package com.test.hotelbay.Review;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.hotelbay.Reservation.Reservation;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReservation(Reservation reservation);
    
    boolean existsByReservationId(Long reservationId);

    void deleteByReservation(Reservation reservation);
}