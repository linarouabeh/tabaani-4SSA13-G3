## Microservice Location de voitures (Gestion de véhicules)

Responsable : Mohamed Gharsa.

Emplacement : `Backend/vehicle-rental-service`.

### Technologies

* Java 17 et Spring Boot 3.5.
* Maven.
* Spring Data JPA et Hibernate.
* MySQL 8 (XAMPP ou Docker).
* Spring Cloud Netflix Eureka (client).
* Spring Boot Actuator.
* Swagger UI (springdoc-openapi).
* API REST.
* Frontend CRUD servi directement par Spring Boot (`static/index.html`).
* Swagger, Postman et curl pour les tests.
* Git et GitHub.

### Entités et fonctionnalités

Entités : `Vehicle`, `VehicleAvailability`, `RentalReservation` et `VehicleInspection`.

Associations :

* Un véhicule possède plusieurs périodes de disponibilité (One-to-Many).
* Un véhicule possède plusieurs réservations (One-to-Many).
* Une réservation possède plusieurs états des lieux : check-in et check-out (One-to-Many).
* `ownerId` et `customerId` sont des références logiques vers le microservice Utilisateurs (pas de clé étrangère entre services).

Fonctionnalités réalisées :

* CRUD des véhicules avec validation des données.
* Recherche par ville, statut et période libre.
* Changement de statut d'un véhicule (`DISPONIBLE`, `LOUE`, `MAINTENANCE`, `RETIRE`).
* Gestion des disponibilités et des périodes bloquées par le propriétaire.
* Réservation avec calcul automatique du montant total.
* Cycle de vie de la réservation : `EN_ATTENTE_CAUTION` → `CONFIRMEE` → `EN_COURS` → `TERMINEE` ou `ANNULEE`.
* États des lieux `CHECK_IN` et `CHECK_OUT`.
* Interface web de gestion de la flotte.
* Enregistrement dans Eureka.
* Deux instances du microservice.
* Supervision avec Actuator.

### Règles métier

| Règle | Code HTTP |
|---|---|
| Champ obligatoire manquant ou invalide | 400 |
| Véhicule ou réservation introuvable | 404 |
| Immatriculation déjà utilisée | 409 |
| Véhicule non disponible (maintenance, retiré, loué) | 409 |
| Période bloquée par le propriétaire | 409 |
| Chevauchement avec une réservation active | 409 |
| Suppression d'un véhicule ayant des réservations actives | 409 |
| Check-in sans réservation `CONFIRMEE` ou check-out sans réservation `EN_COURS` | 409 |
| Annulation d'une réservation `EN_COURS`, `TERMINEE` ou `ANNULEE` | 409 |
| Création d'une réservation acceptée, en attente de la caution | 202 |

Le montant total est égal au prix par jour multiplié par le nombre de jours (24 h commencées, minimum 1 jour). Les erreurs sont renvoyées au format Problem Details (RFC 9457).

---

## Configuration MySQL

### Prérequis

Démarrer MySQL dans XAMPP (port 3306) ou avec Docker :

```powershell
docker compose up -d
```

La base `vehicle_rental_db` est créée automatiquement (`createDatabaseIfNotExist=true`). Elle peut aussi être créée à la main dans phpMyAdmin avec le script `database/create-database.sql` :

```sql
CREATE DATABASE IF NOT EXISTS vehicle_rental_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

### Connexion du microservice

La configuration utilise :

```yaml
spring.datasource.url: ${DB_URL:jdbc:mysql://127.0.0.1:3306/vehicle_rental_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username: ${DB_USERNAME:root}
spring.datasource.password: ${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto: update
```

Si MySQL utilise un mot de passe ou un autre port, définir les variables dans le même terminal avant le lancement :

```powershell
$env:DB_URL = "jdbc:mysql://127.0.0.1:3306/vehicle_rental_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "MOT_DE_PASSE_LOCAL"
```

Le mot de passe ne doit pas être enregistré dans le dépôt Git.

Les deux instances utilisent la même base MySQL. Les données sont partagées et conservées après le redémarrage des applications.

Hibernate crée et met à jour les tables avec `ddl-auto=update`. Au premier démarrage, le `DataSeeder` insère trois véhicules de démonstration si la table `vehicles` est vide.

Un profil `h2` (base en mémoire) permet de lancer le service sans MySQL pour une démonstration.

---

## Lancement local avec Eureka

### Prérequis

* JDK 17 et Maven 3.9+.
* MySQL démarré.
* Variable `DB_PASSWORD` configurée si nécessaire.

Les deux serveurs Eureka se répliquent via `localhost` : aucune modification du fichier `hosts` n'est nécessaire.

### Serveurs Eureka

| Configuration | Profil actif | Port |
|---|---|---|
| Eureka peer1 | `peer1` | 8761 |
| Eureka peer2 | `peer2` | 8762 |

```powershell
cd eureka-server
mvn spring-boot:run -Dspring-boot.run.profiles=peer1
mvn spring-boot:run -Dspring-boot.run.profiles=peer2
```

### Instances du microservice

| Configuration | Profil actif | Port |
|---|---|---|
| Vehicle instance 1 | aucun | 8082 |
| Vehicle instance 2 | `instance2` | 8083 |

```powershell
cd vehicle-rental-service
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=instance2
```

Chaque instance a un `instance-id` unique (`VEHICLE-RENTAL-SERVICE:8082` et `VEHICLE-RENTAL-SERVICE:8083`) et s'enregistre auprès des deux serveurs Eureka.

### Lancement automatique (Windows)

```powershell
scripts\start-all.bat
```

Le script démarre les deux Eureka puis les deux instances. L'option `scripts\start-all.bat h2` utilise la base H2.

### Ordre de lancement

1. Démarrer MySQL.
2. Lancer Eureka peer1.
3. Lancer Eureka peer2.
4. Lancer l'instance 1 (8082).
5. Lancer l'instance 2 (8083).

Garder les quatre applications Java et MySQL en marche simultanément.

### Vérifications Eureka

* [Dashboard peer1](http://localhost:8761)
* [Dashboard peer2](http://localhost:8762)

Après le démarrage et la propagation des enregistrements, les deux dashboards doivent afficher :

| Application | Statut attendu | Ports |
|---|---|---|
| EUREKA-SERVER | UP (2) | 8761 et 8762 |
| VEHICLE-RENTAL-SERVICE | UP (2) | 8082 et 8083 |

Dans DS Replicas, peer1 référence peer2 et peer2 référence peer1.

### Captures Eureka

Eureka peer1

![Eureka peer1](Documentation/captures/vehicle-rental/eureka-peer1.png)

Eureka peer2

![Eureka peer2](Documentation/captures/vehicle-rental/eureka-peer2.png)

---

## API REST

URL de base : `http://localhost:8082`.

Les mêmes routes sont disponibles sur le port 8083.

Swagger UI : [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html).

### Véhicules

| Méthode | Route | Fonction |
|---|---|---|
| POST | `/api/vehicules` | Ajouter un véhicule |
| GET | `/api/vehicules` | Lister et rechercher (`city`, `status`, `from`, `to`) |
| GET | `/api/vehicules/{id}` | Consulter un véhicule |
| PUT | `/api/vehicules/{id}` | Modifier un véhicule |
| PATCH | `/api/vehicules/{id}/status` | Changer le statut |
| DELETE | `/api/vehicules/{id}` | Supprimer un véhicule |
| POST | `/api/vehicules/{id}/disponibilites` | Ajouter une période de disponibilité ou de blocage |
| GET | `/api/vehicules/{id}/disponibilites` | Lister les disponibilités |

### Locations

| Méthode | Route | Fonction |
|---|---|---|
| POST | `/api/locations` | Demander une réservation (202) |
| GET | `/api/locations` | Lister les réservations (`customerId` optionnel) |
| GET | `/api/locations/{id}` | Consulter une réservation |
| PATCH | `/api/locations/{id}/confirmation` | Confirmer (caution payée) |
| PATCH | `/api/locations/{id}/annulation` | Annuler |
| POST | `/api/locations/{id}/inspections` | État des lieux (`CHECK_IN` ou `CHECK_OUT`) |

### Exemple de requête

```bash
curl -i -X POST http://localhost:8082/api/vehicules -H "Content-Type: application/json" -d '{
 "make":"Kia","model":"Picanto","registrationNumber":"301 TUN 1111","bodyType":"Citadine",
 "seatingCapacity":4,"transmissionType":"MANUELLE","fuelType":"ESSENCE","city":"Tunis",
 "pricePerDay":80,"depositAmount":250,"ownerId":1}'
```

### Captures Swagger

Swagger UI du microservice

![Swagger UI](Documentation/captures/vehicle-rental/swagger-ui.png)

---

## Interface de gestion de la flotte

L'interface est servie par Spring Boot, sans Node ni npm :

* http://localhost:8082/
* http://localhost:8083/

Fonctions :

* **Create** : ajout avec validation des champs et message d'erreur lisible (ex. immatriculation déjà utilisée).
* **Read** : tableau, recherche, filtres ville et statut, statistiques de la flotte et fiche détail.
* **Update** : formulaire prérempli pour modifier les informations et le statut.
* **Delete** : confirmation avant suppression et gestion des conflits.

### Captures de l'interface

Tableau de bord et liste des véhicules

![Liste des véhicules](Documentation/captures/vehicle-rental/vehicle-dashboard.png)

Ajout d'un véhicule

![Ajout d'un véhicule](Documentation/captures/vehicle-rental/vehicle-create.png)

Modification d'un véhicule

![Modification d'un véhicule](Documentation/captures/vehicle-rental/vehicle-update.png)

Suppression d'un véhicule

![Suppression d'un véhicule](Documentation/captures/vehicle-rental/vehicle-delete.png)

---

## Scénario de test

```bash
# Créer un véhicule -> 201 + en-tête Location
# (voir l'exemple ci-dessus)

# Lister et rechercher
curl "http://localhost:8082/api/vehicules?city=Tunis"
curl "http://localhost:8082/api/vehicules?from=2026-12-10T09:00:00&to=2026-12-13T09:00:00"

# Réserver -> 202, total = 3 jours x prix par jour
curl -i -X POST http://localhost:8082/api/locations -H "Content-Type: application/json" -d '{
 "vehicleId":1,"customerId":5,"pickupDateTime":"2026-12-10T09:00:00","returnDateTime":"2026-12-13T09:00:00"}'

# Même période -> 409 Conflict

# Confirmer la caution, puis check-in et check-out
curl -X PATCH http://localhost:8082/api/locations/1/confirmation
curl -X POST http://localhost:8082/api/locations/1/inspections -H "Content-Type: application/json" \
 -d '{"inspectionType":"CHECK_IN","odometerReading":12000,"inspectionResult":"CONFORME"}'
curl -X POST http://localhost:8082/api/locations/1/inspections -H "Content-Type: application/json" \
 -d '{"inspectionType":"CHECK_OUT","odometerReading":12450,"inspectionResult":"CONFORME"}'
```

Erreurs vérifiées :

* 400 : champ obligatoire vide.
* 404 : `GET /api/vehicules/999`.
* 409 : réservation sur une période déjà occupée.

### Captures des tests

Réservation créée (202)

![Réservation 202](Documentation/captures/vehicle-rental/reservation-202.png)

Chevauchement de réservation (409)

![Conflit 409](Documentation/captures/vehicle-rental/reservation-409.png)

---

## Vérifications MySQL réalisées

* Connexion MySQL : UP dans Actuator.
* Création d'un véhicule via l'instance 8082.
* Consultation de ce véhicule via l'instance 8083.
* Conservation des données après redémarrage des instances.
* Création d'une réservation associée à un véhicule.
* Vérification des tables et de `vehicle_id` dans phpMyAdmin.

### Données enregistrées dans MySQL

Table `vehicles`

![Table vehicles](Documentation/captures/vehicle-rental/mysql-vehicles.png)

Table `rental_reservations`

![Table rental_reservations](Documentation/captures/vehicle-rental/mysql-reservations.png)

---

## Supervision Actuator

| Endpoint | Fonction |
|---|---|
| `/actuator/health` | Santé du microservice et de ses composants (base de données, disque) |
| `/actuator/info` | Informations sur l'application |
| `/actuator/metrics` | Liste des métriques |
| `/actuator/metrics/jvm.memory.used` | Mémoire utilisée par la JVM |
| `/actuator/mappings` | Routes exposées par le microservice |
| `/actuator/loggers` | Consultation et modification du niveau des logs |

### Liens

* [Santé — 8082](http://localhost:8082/actuator/health)
* [Santé — 8083](http://localhost:8083/actuator/health)
* [Informations](http://localhost:8082/actuator/info)
* [Métriques](http://localhost:8082/actuator/metrics)
* [Mémoire JVM](http://localhost:8082/actuator/metrics/jvm.memory.used)
* [Mappings](http://localhost:8082/actuator/mappings)
* [Logs](http://localhost:8082/actuator/loggers)

### Captures Actuator

Santé du microservice

![Actuator Health](Documentation/captures/vehicle-rental/actuator-health.png)

Informations de l'application

![Actuator Info](Documentation/captures/vehicle-rental/actuator-info.png)

Métriques

![Actuator Metrics](Documentation/captures/vehicle-rental/actuator-metrics.png)

Niveau des logs

![Actuator Loggers](Documentation/captures/vehicle-rental/actuator-loggers.png)

---

## Intégration du groupe

Cette documentation présente la configuration locale du microservice Location de voitures et des deux serveurs Eureka.

L'intégration complète du groupe reste à vérifier :

* Enregistrement simultané de tous les microservices.
* Microservice développé avec une autre technologie.
* Deux instances d'un deuxième microservice fortement sollicité.
* Fonctionnement sur les machines des membres.

Évolutions prévues : appel OpenFeign vers le microservice Utilisateurs avec Resilience4j, messagerie RabbitMQ pour la caution, API Gateway, Keycloak et Config Server.
