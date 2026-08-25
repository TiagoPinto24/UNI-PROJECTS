package com.test.hotelbay.Reservation;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Room.Room;

@Entity
public class Reservation {

    public Reservation() {
    }

    public Reservation(Room room, Account account, LocalDate checkin, LocalDate checkout, ReservationStatus status) {
        this.room = room;
        this.account = account;
        this.checkin = checkin;
        this.checkout = checkout;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Account account;

    private LocalDate checkin;
    private LocalDate checkout;

    private ReservationStatus status;

    private Integer numberGuests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Integer getNumberGuests() {
        return numberGuests;
    }

    public void setNumberGuests(Integer numberGuests) {
        this.numberGuests = numberGuests;
    }
}