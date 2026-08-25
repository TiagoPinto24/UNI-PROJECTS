package com.test.hotelbay.Reservation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/reservations")
public class TestReservationController {

    private final ReservationRepository reservationRepository;

    public TestReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public Map<String, Object> createTestReservation(@RequestBody Map<String, Object> request) {

        Reservation reservation = new Reservation();

        reservation.setStatus(
                ReservationStatus.valueOf(request.get("status").toString()));

        reservation = reservationRepository.save(reservation);

        Map<String, Object> response = new HashMap<>();
        response.put("id", reservation.getId());
        response.put("status", reservation.getStatus().name());

        return response;
    }
}