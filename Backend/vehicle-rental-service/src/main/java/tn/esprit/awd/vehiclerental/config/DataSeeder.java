package tn.esprit.awd.vehiclerental.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.awd.vehiclerental.entity.Vehicle;
import tn.esprit.awd.vehiclerental.repository.VehicleRepository;
import java.math.BigDecimal;

/** Données de démonstration (insérées uniquement si la table est vide). */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(VehicleRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(v("Renault", "Clio 5", "123 TUN 4567", "Citadine", 5, "MANUELLE", "ESSENCE", "Tunis", "90.00", "300.00", 1L));
            repo.save(v("Peugeot", "208", "210 TUN 8890", "Citadine", 5, "AUTOMATIQUE", "DIESEL", "Sousse", "110.00", "400.00", 2L));
            repo.save(v("Dacia", "Duster", "187 TUN 3321", "SUV", 5, "MANUELLE", "DIESEL", "Tunis", "140.00", "600.00", 1L));
        };
    }

    private Vehicle v(String make, String model, String reg, String body, int seats, String gear, String fuel,
                      String city, String price, String deposit, Long owner) {
        Vehicle x = new Vehicle();
        x.setMake(make); x.setModel(model); x.setRegistrationNumber(reg); x.setBodyType(body);
        x.setSeatingCapacity(seats); x.setTransmissionType(gear); x.setFuelType(fuel); x.setCity(city);
        x.setPricePerDay(new BigDecimal(price)); x.setDepositAmount(new BigDecimal(deposit)); x.setOwnerId(owner);
        return x;
    }
}
