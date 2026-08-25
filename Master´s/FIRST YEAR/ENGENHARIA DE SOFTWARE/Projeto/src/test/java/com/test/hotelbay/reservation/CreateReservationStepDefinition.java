package com.test.hotelbay.reservation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Room.RoomStatus;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Account.AccountType;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateReservationStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private RoomRepository roomRepository;

        @Autowired
        private AccountRepository accountRepository;

        private ResultActions action;

        private Room room;
        private Account account;

        @Given("the create reservation service URL")
        public void the_create_reservation_service_url() {
                assertNotNull(mvc);
        }

        @Given("a room is available between {string} and {string}")
        public void a_room_is_available_between_and(String startDate, String endDate) {

                room = new Room(startDate, endDate, 2, null, null, null, null);

                room.setNumber("101");
                room.setStatus(RoomStatus.AVAILABLE);

                room = roomRepository.save(room);
        }

        @Given("a registered guest exists")
        public void a_registered_guest_exists() {

                account = new Account(
                                "john@email.com",
                                "John Doe",
                                "JohnDoe123",
                                "password",
                                AccountType.GUEST);

                account = accountRepository.save(account);
        }

        @When("the client calls /reservations with valid reservation data")
        public void the_client_calls_reservations() throws Exception {

                String requestBody = "{"
                                + "\"room\":" + room.getId() + ","
                                + "\"guest\":" + account.getId() + ","
                                + "\"checkInDate\":\"2026-07-01\","
                                + "\"checkOutDate\":\"2026-07-05\","
                                + "\"numberGuests\":2"
                                + "}";

                action = mvc.perform(
                                post("/reservations")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody));
        }

        @Then("the create reservation client receives status code {int}")
        public void the_create_reservation_client_receives_status_code(Integer statusCode) throws Exception {
                action.andExpect(status().is(statusCode));
        }

        @Then("the reservation is created with status {string}")
        public void the_reservation_is_created_with_status(String expectedStatus) throws Exception {
                action.andExpect(jsonPath("$.status", Matchers.is(expectedStatus)));
        }
}