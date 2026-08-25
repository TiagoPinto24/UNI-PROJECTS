package com.test.hotelbay.rooms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RoomDeletionStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public RoomDeletionStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RoomRepository roomRepository;

    String requestBody;

    @When("the client calls room\\/delete")
    public void when_the_client_calls_hotel_room_delete() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());
        String roomId = String.valueOf(context.getExistingRoom().getId());

        ResultActions action = mvc.perform(delete(context.getUrl() + "/room/delete/" + roomId)
            .header("X-ADMIN-ID", adminId)
            .header("X-HOTEL-ID", hotelId)
            .contentType(MediaType.APPLICATION_JSON));
        context.setResultActions(action);
    }

    @Then("the room is deleted from the system")
    public void and_the_room_is_deleted_from_the_system() {
        Optional<Room> roomOptional = roomRepository.findByNumberAndHotel(context.getExistingRoom().getNumber(),context.getExistingHotel());
        assertFalse(roomOptional.isPresent());
    }
}
