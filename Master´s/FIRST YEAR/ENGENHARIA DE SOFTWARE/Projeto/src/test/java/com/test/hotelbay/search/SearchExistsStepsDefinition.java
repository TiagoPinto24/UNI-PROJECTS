package com.test.hotelbay.search;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasItem;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomStatus;
import com.test.hotelbay.general.helpers.DeletRoomHelper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SearchExistsStepsDefinition extends StepDefinition{
    private final ScenarioContext context;
    private final DeletRoomHelper deleteHelper;

    public SearchExistsStepsDefinition(ScenarioContext context, DeletRoomHelper deleteHelper) {
        this.context = context;
        this.deleteHelper = deleteHelper;
    }

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    
    LocalDate checkIn = LocalDate.of(2027, 1, 1);
    LocalDate checkOut = LocalDate.of(2027, 1, 8);

    @Transactional
    @Given("a room with the number {int} available between {int}\\/{int}\\/{int} and {int}\\/{int}\\/{int} in the location Lisbon belongs to Grand Hotel")
    public void and_room_with_number_121_available_location_Lisbon_belongs_grand_hotel(Integer roomNumber, Integer d1, Integer d2, Integer m1, Integer m2, Integer y1, Integer y2) {
        Room existingRoom = new Room("121", "A luxurious room with a nice view to the city", 2, new BigDecimal(599.00), RoomStatus.AVAILABLE, new ArrayList<>(), context.getExistingHotel());
        if (roomRepository.findByNumberAndHotel(existingRoom.getNumber(),context.getExistingHotel()).isEmpty()) {
            roomRepository.save(existingRoom);
        } else {
            deleteHelper.deleteRoom(existingRoom.getNumber(), context.getExistingHotel());
            roomRepository.deleteByNumberAndHotel(existingRoom.getNumber(), context.getExistingHotel()); 
            roomRepository.save(existingRoom);
        }
        context.setExistingRoom(existingRoom);
        if (!reservationRepository.findByRoomAndCheckinAndCheckout(existingRoom, checkIn, checkOut).isEmpty()) {
            reservationRepository.deleteByRoomAndCheckinAndCheckout(existingRoom,checkIn,checkOut);
        }
    }

    @Then("contain the information that this room is available in this time period")
    public void and_contain_the_information_that_this_room_is_available_n_this_tome_period() throws Exception {
        context.getResultActions().andExpect(jsonPath("$.rooms[*].number", hasItem("121")));
        context.getResultActions().andExpect(jsonPath("$.rooms[*].status", hasItem("AVAILABLE")));
    }

}
