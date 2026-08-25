package com.test.hotelbay.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.test.hotelbay.Reservation.Reservation;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Reservation.ReservationStatus;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> request) {

        Long reservationId = Long.valueOf(request.get("reservationId").toString());

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setStatus("PENDING");

        payment = paymentRepository.save(payment);

        Map<String, Object> response = new HashMap<>();
        response.put("paymentId", payment.getId());
        response.put("paymentStatus", payment.getStatus());

        return response;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> changeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Payment not found"));

        String status = request.get("status").toString().toLowerCase();
        payment.setStatus(status);

        Reservation reservation = payment.getReservation();

        Map<String, Object> response = new HashMap<>();

        if ("failed".equals(status)) {
            paymentRepository.save(payment);

            response.put("paymentStatus", payment.getStatus());
            response.put("reservationStatus", reservation.getStatus().name());

            return ResponseEntity.unprocessableEntity().body(response);
        }

        if ("successful".equals(status)) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }

        paymentRepository.save(payment);
        reservationRepository.save(reservation);

        response.put("paymentStatus", payment.getStatus());
        response.put("reservationStatus", reservation.getStatus().name());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reservationId}")
    public List<Payment> getPaymentsByReservation(@PathVariable Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }
}