package com.test.hotelbay.general;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomStatus;

import io.cucumber.java.en.Given;

public class RoomGeneralStepsDefinition {
    private final ScenarioContext context;

    public RoomGeneralStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    @Given("a room with the number 121 belongs to Grand Hotel")
    public void and_a_room_with_the_number_121_belongs_to_Grand_Hotel() {
        Room existingRoom = new Room("121", "A luxurious room with a nice view to the city", 2, new BigDecimal(599.00), RoomStatus.AVAILABLE, new ArrayList<>(List.of()), context.getExistingHotel());
        if (roomRepository.findByNumberAndHotel(existingRoom.getNumber(),context.getExistingHotel()).isEmpty()) {
            roomRepository.save(existingRoom);
        } else {
            roomRepository.deleteByNumberAndHotel(existingRoom.getNumber(), context.getExistingHotel());
            roomRepository.save(existingRoom);
        }
        context.setExistingRoom(existingRoom);
    }
}
