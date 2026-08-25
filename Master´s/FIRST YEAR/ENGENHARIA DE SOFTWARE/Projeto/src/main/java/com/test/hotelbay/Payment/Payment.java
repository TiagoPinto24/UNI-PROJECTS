package com.test.hotelbay.Payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.test.hotelbay.Reservation.Reservation;

@Entity
public class Payment {

    public Payment() {
    }

    public Payment(Long transactionNumber, Reservation reservation, String status) {
        this.transactionNumber = transactionNumber;
        this.reservation = reservation;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Reservation reservation;

    private String status;
    private Long transactionNumber;

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTransactionNumber() {
        return this.transactionNumber;
    }

    public void setTransactionNumber(Long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

}