package com.test.hotelbay.Review;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

        @Autowired
        private ReviewRepository reviewRepository;

        @PostMapping
        public ResponseEntity<?> createReview(@RequestBody Map<String, Object> body) {

                Long reservationId = Long.valueOf(body.get("reservationId").toString());

                if (reviewRepository.existsByReservationId(reservationId)) {
                        return ResponseEntity.status(409).body("Reservation already reviewed");
                }

                Map<String, Object> response = new HashMap<>();

                response.put("reservationId", reservationId);
                response.put("hotel", body.get("hotel"));
                response.put("nameGuest", body.get("nameGuest"));
                response.put("textualDescription", body.get("textualDescription"));
                response.put("rating", body.get("rating"));

                return ResponseEntity.status(201).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateReview(
                        @PathVariable Long id,
                        @RequestBody Map<String, Object> body) {

                Map<String, Object> response = new HashMap<>();
                response.put("rating", body.get("rating"));

                return ResponseEntity.ok(response);
        }
}