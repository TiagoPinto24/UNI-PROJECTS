package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class InfoReservationStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private RoomRepository roomRepository;

        @Autowired
        private AccountRepository accountRepository;

        private ResultActions action;
        private Integer reservationId;

        private Long roomId;
        private Long guestId;

        @Given("the info reservation service URL")
        public void the_info_reservation_service_url() {
        }

        @Given("an info reservation exists")
        public void an_info_reservation_exists() throws Exception {

                Room room = new Room();
                room = roomRepository.save(room);
                roomId = room.getId();

                Account account = new Account();
                account = accountRepository.save(account);
                guestId = account.getId();

                Map<String, Object> payload = new HashMap<>();

                payload.put("room", roomId);
                payload.put("guest", guestId);
                payload.put("checkInDate", "2026-05-10");
                payload.put("checkOutDate", "2026-05-15");
                payload.put("numberGuests", 2);

                String body = objectMapper.writeValueAsString(payload);

                String response = mvc.perform(
                                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                                .post("/reservations")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(body))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode json = objectMapper.readTree(response);

                if (json.get("id") == null) {
                        throw new RuntimeException("Reservation ID not returned from POST /reservations");
                }

                reservationId = json.get("id").asInt();
        }

        @When("the info reservation client calls GET reservation details")
        public void the_info_reservation_client_calls_get() throws Exception {

                action = mvc.perform(
                                get("/reservations/" + reservationId)
                                                .contentType(MediaType.APPLICATION_JSON));
        }

        @Then("the info reservation client receives status code {int}")
        public void the_info_reservation_client_receives_status_code(Integer code) throws Exception {

                action.andExpect(status().is(code));
        }

        @Then("the info reservation response contains fields:")
        public void the_info_reservation_response_contains_fields(DataTable table) throws Exception {

                List<String> fields = table.asList();

                for (String field : fields) {

                        action.andExpect(
                                        org.springframework.test.web.servlet.result.MockMvcResultMatchers
                                                        .jsonPath("$." + field)
                                                        .exists());
                }
        }
}