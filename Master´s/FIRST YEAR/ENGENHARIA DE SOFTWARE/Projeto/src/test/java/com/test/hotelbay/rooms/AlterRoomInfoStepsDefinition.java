package com.test.hotelbay.rooms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AlterRoomInfoStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public AlterRoomInfoStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    String requestBody;

    BigDecimal newPrice = new BigDecimal("499.99");

    @When("the client gives valid information to alter the room")
    public void when_the_client_gives_valid_informtation_to_alter_the_room() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("price", newPrice);

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls room\\/changeInfo")
    public void and_calls_hotel_hotelID_room_changeInfo() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());
        String roomId = String.valueOf(context.getExistingRoom().getId());

        ResultActions action = mvc.perform(put(context.getUrl() + "/room/changeInfo/" + roomId)
            .header("X-ADMIN-ID", adminId)
            .header("X-HOTEL-ID", hotelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }

    @Then("the information related to the room 121 is changed")
    public void and_the_information_related_to_the_room_121_is_changed() {
        Optional<Room> roomOptional = roomRepository.findByNumberAndHotel("121", context.getExistingHotel());
        assertTrue(roomOptional.isPresent());

        Room room = roomOptional.get();
        assertEquals(new BigDecimal("499.99"), room.getPrice());
    }
}
