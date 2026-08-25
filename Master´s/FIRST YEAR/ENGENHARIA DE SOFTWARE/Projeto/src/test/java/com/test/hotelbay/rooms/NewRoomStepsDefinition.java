package com.test.hotelbay.rooms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.test.hotelbay.Room.RoomStatus;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NewRoomStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public NewRoomStepsDefinition(ScenarioContext context) {
        this.context = context;
    }
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    String requestBody;

    @When("the client gives valid informations for a new room")
    public void when_the_client_gives_valid_informations_for_a_new_room() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("number", "121");
        payload.put("description", "A luxurious room with a nice view to the city");
        payload.put("capacity", 2);
        payload.put("price", new BigDecimal("599.00"));
        payload.put("status", RoomStatus.AVAILABLE);
        payload.put("categories", new ArrayList<>(List.of()));
        
        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls room\\/create")
    public void and_calls_room_hotelID_create() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());

        ResultActions action = mvc.perform(post(context.getUrl() + "/room/create")
            .header("X-ADMIN_ID", adminId)
            .header("X-HOTEL-ID", hotelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }


    @Then("a new room is created")
    public void and_a_new_room_is_created() throws Exception{
        Optional<Room> roomOptional = roomRepository.findByNumberAndHotel("121", context.getExistingHotel());
        assertTrue(roomOptional.isPresent());

        Room room = roomOptional.get();
        assertEquals("A luxurious room with a nice view to the city", room.getDescription());
        assertEquals(2, room.getCapacity());
        assertEquals(new BigDecimal("599.00"), room.getPrice());
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertEquals(context.getExistingHotel().getId(), room.getHotel().getId());
    }
}
