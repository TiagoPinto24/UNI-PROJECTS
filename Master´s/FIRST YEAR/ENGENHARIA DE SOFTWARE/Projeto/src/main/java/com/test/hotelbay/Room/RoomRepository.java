package com.test.hotelbay.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.test.hotelbay.Hotel.Hotel;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long>{

    Optional<Room> findByNumberAndHotel(String number, Hotel hotel);

    void deleteByNumberAndHotel(String number, Hotel hotel);

    @Query(value = 
        "SELECT r FROM Room r JOIN r.hotel h LEFT JOIN r.categories c " +
        "WHERE (:hotelName IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :hotelName, '%'))) " + 
        "AND (:location IS NULL OR LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
        "AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
        "AND (:maxPrice IS NULL OR r.price <= :maxPrice) AND (:minPrice IS NULL OR r.price >= :minPrice) " +
        "AND (:capacity IS NULL OR r.capacity >= :capacity)"
    )
    List<Room> searchRooms(String hotelName, String location, String categoryName, BigDecimal maxPrice, BigDecimal minPrice,
            Integer capacity);
    
}
