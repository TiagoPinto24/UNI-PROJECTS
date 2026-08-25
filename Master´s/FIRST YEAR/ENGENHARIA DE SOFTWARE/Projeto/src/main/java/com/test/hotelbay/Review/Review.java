package com.test.hotelbay.Review;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.test.hotelbay.Reservation.Reservation;

@Entity
public class Review {

    public Review() {
    }

    public Review(Reservation reservation, String textualDescription, Integer rating) {
        this.reservation = reservation;
        this.textualDescription = textualDescription;
        this.rating = rating;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Reservation reservation;

    private String textualDescription;

    private Integer rating;

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getTextualDescription() {
        return textualDescription;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Object getGuestName() {
        throw new UnsupportedOperationException("Unimplemented method 'getGuestName'");
    }

    public Object getHotelName() {
        throw new UnsupportedOperationException("Unimplemented method 'getHotelName'");
    }
}