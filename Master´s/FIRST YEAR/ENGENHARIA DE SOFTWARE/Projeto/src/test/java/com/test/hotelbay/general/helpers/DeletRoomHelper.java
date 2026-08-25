package com.test.hotelbay.general.helpers;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Reservation.Reservation;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

@Service
public class DeletRoomHelper {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public DeletRoomHelper(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void deleteRoom(String roomNumber, Hotel hotel) {
        Optional<Room> roomOptional = roomRepository.findByNumberAndHotel(roomNumber, hotel);
        if (roomOptional.isEmpty()) {
            fail("TEST ERROR: Admin account was not created before this step");
        } else {
            
            Room room = roomOptional.get();

            List<Reservation> reservations = reservationRepository.findByRoom(room);

            for (Reservation reservation : reservations) {
                reservationRepository.delete(reservation);
            }
        }

    }
}
