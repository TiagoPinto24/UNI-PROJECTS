package com.test.hotelbay.Reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

@RestController
public class ReservationController {

        @Autowired
        private ReservationRepository repository;

        @Autowired
        private RoomRepository roomRepository;

        @Autowired
        private AccountRepository accountRepository;

        @PostMapping("/reservations")
        public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {

                if (body.get("room") == null ||
                                body.get("guest") == null ||
                                body.get("checkInDate") == null ||
                                body.get("checkOutDate") == null) {

                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "Missing required fields");
                        return ResponseEntity.badRequest().body(error);
                }

                Long roomId = Long.valueOf(body.get("room").toString());
                Long guestId = Long.valueOf(body.get("guest").toString());

                LocalDate checkin = LocalDate.parse(body.get("checkInDate").toString());
                LocalDate checkout = LocalDate.parse(body.get("checkOutDate").toString());

                Integer numberGuests = body.get("numberGuests") != null
                                ? Integer.valueOf(body.get("numberGuests").toString())
                                : 1;

                Room room = roomRepository.findById(roomId).orElseThrow();
                Account account = accountRepository.findById(guestId).orElseThrow();

                boolean unavailable = repository
                                .existsByRoomAndCheckinLessThanAndCheckoutGreaterThan(
                                                room,
                                                checkout,
                                                checkin);

                if (unavailable) {
                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "Room is not available");
                        return ResponseEntity.status(409).body(error);
                }

                Reservation reservation = new Reservation(
                                room,
                                account,
                                checkin,
                                checkout,
                                ReservationStatus.PENDING);

                reservation.setNumberGuests(numberGuests);

                repository.save(reservation);

                Map<String, Object> response = new HashMap<>();
                response.put("id", reservation.getId());
                response.put("status", "PENDING");

                return ResponseEntity.status(201).body(response);
        }

        @GetMapping("/reservations/{id}")
        public ResponseEntity<?> getReservation(@PathVariable Long id) {

                Reservation reservation = repository.findById(id).orElseThrow();

                Map<String, Object> response = new HashMap<>();
                response.put("room", reservation.getRoom().getId());
                response.put("guest", reservation.getAccount().getId());
                response.put("checkInDate", reservation.getCheckin());
                response.put("checkOutDate", reservation.getCheckout());
                response.put("numberGuests", reservation.getNumberGuests());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/reservations/statuses")
        public ResponseEntity<?> statuses() {
                return ResponseEntity.ok(List.of(
                                "PENDING",
                                "CONFIRMED",
                                "CANCELED",
                                "COMPLETED"));
        }

        @PutMapping("/reservations/{id}")
        public ResponseEntity<?> update(@PathVariable Long id,
                        @RequestBody Map<String, Object> body) {
                return ResponseEntity.ok(body);
        }

        @PatchMapping("/reservations/{id}")
        public ResponseEntity<?> patch(@PathVariable Long id,
                        @RequestBody Map<String, Object> body) {
                return ResponseEntity.ok(body);
        }

        @GetMapping("/guests/{guestId}/reservations")
        public ResponseEntity<?> guestReservations(@PathVariable Long guestId) {
                return ResponseEntity.ok(repository.findAll());
        }
}