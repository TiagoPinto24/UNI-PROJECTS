package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomStatus;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PreventReservationStepDefinition extends StepDefinition {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions action;

    private Long roomId;
    private Long guestId;

    @Given("the prevent reservation service URL")
    public void the_prevent_reservation_service_url() {
    }

    @Given("a room is already reserved between {string} and {string}")
    public void a_room_is_already_reserved_between_and(String checkIn, String checkOut) throws Exception {

        Hotel hotel = new Hotel();
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setNumber("101");
        room.setDescription("Test room");
        room.setCapacity(2);
        room.setPrice(new BigDecimal("100.00"));
        room.setStatus(RoomStatus.AVAILABLE);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        Account account = new Account();
        account.setName("Test User");
        account.setEmail("test@test.com");
        account = accountRepository.save(account);

        roomId = room.getId();
        guestId = account.getId();

        Map<String, Object> payload = new HashMap<>();
        payload.put("room", roomId);
        payload.put("guest", guestId);
        payload.put("checkInDate", checkIn);
        payload.put("checkOutDate", checkOut);
        payload.put("numberGuests", 2);

        String requestBody = objectMapper.writeValueAsString(payload);

        mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @When("the client calls /reservations with:")
    public void the_client_calls_create_reservation(DataTable table) throws Exception {

        Map<String, String> data = table.asMap(String.class, String.class);

        Map<String, Object> payload = new HashMap<>();

        payload.put("room", roomId);
        payload.put("guest", guestId);

        payload.put("checkInDate", data.get("checkIn"));
        payload.put("checkOutDate", data.get("checkOut"));
        payload.put("numberGuests", data.get("numberGuests"));

        String requestBody = objectMapper.writeValueAsString(payload);

        action = mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Then("the prevent reservation client receives status code {int}")
    public void the_prevent_reservation_client_receives_status_code(Integer statusCode) throws Exception {
        action.andExpect(status().is(statusCode));
    }

    @Then("the prevent reservation response contains message {string}")
    public void the_prevent_reservation_response_contains_message(String message) throws Exception {
        action.andExpect(jsonPath("$.message").value(message));
    }
}