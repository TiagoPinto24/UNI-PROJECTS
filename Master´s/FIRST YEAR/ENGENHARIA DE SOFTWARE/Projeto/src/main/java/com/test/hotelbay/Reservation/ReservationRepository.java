package com.test.hotelbay.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Room.Room;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRoom(Room room);

    List<Reservation> findByAccount(Account guest);

    Optional<Reservation> findByIdAndAccount(Long id, Account account);

    void deleteByIdAndAccount(Long id, Account account);

    Optional<Reservation> findByRoomAndCheckinAndCheckout(Room room, LocalDate checkin, LocalDate checkout);

    void deleteByRoomAndCheckinAndCheckout(Room room, LocalDate checkin, LocalDate checkout);

    void deleteByRoom(Room room);

    @Query(value =
        "SELECT r FROM Room r WHERE NOT EXISTS (SELECT res FROM Reservation res WHERE res.room = r AND res.checkin < :checkout AND res.checkout > :checkin)"
    )
        List<Room> searchAvailableRooms(
            @Param("checkin") LocalDate checkin,
            @Param("checkout") LocalDate checkout
        );

    boolean existsByRoomAndCheckinLessThanAndCheckoutGreaterThan(Room room, LocalDate checkout, LocalDate checkin);
}