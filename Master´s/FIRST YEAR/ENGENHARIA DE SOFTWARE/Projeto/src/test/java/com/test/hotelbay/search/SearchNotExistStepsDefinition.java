package com.test.hotelbay.search;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.general.helpers.DeletRoomHelper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class SearchNotExistStepsDefinition extends StepDefinition{
    private final ScenarioContext context;
    private final DeletRoomHelper deleteHelper;

    public SearchNotExistStepsDefinition(ScenarioContext context, DeletRoomHelper deleteHelper) {
        this.context = context;
        this.deleteHelper = deleteHelper;
    }

    @Autowired
    private RoomRepository roomRepository;    

    @Given("no room is available between {int}\\/{int}\\/{int} and {int}\\/{int}\\/{int} in the location Lisbon")
    public void and_no_room_is_available_between_in_the_location_lisbon(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5, Integer int6) {
        List<Room> allRooms = roomRepository.findAll();
        System.out.println(allRooms.size());
       
        for (Room room : allRooms) {
            if (room.getNumber() != null) {
                System.out.println("\b\n\n\n\n\n\n\n" + room.getNumber());
                deleteHelper.deleteRoom(room.getNumber(), room.getHotel());
                roomRepository.delete(room);
            }
        }

    }

    @Then("the response should not contain any rooms")
    public void and_the_response_should_not_contain_any_rooms() throws Exception {
        context.getResultActions().andExpect(jsonPath("$.rooms").isEmpty());
    }
}
