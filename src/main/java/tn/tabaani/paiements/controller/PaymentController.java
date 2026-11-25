
package tn.tabaani.paiements.controller;

import tn.tabaani.paiements.entity.Payment;
import tn.tabaani.paiements.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Ajouter un paiement
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }

    // Récupérer tous les paiements
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Récupérer un paiement par ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Modifier un paiement
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment) {

        if (paymentService.getPaymentById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        payment.setId(id);
        Payment updatedPayment = paymentService.savePayment(payment);

        return ResponseEntity.ok(updatedPayment);
    }

    // Supprimer un paiement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {

        if (paymentService.getPaymentById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

