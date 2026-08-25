package com.test.hotelbay.reviews;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class AssociateReviewStepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        private ResultActions action;

        @Given("a completed reservation exists for association")
        public void a_completed_reservation_exists_for_association() {
        }

        @Given("the hotel is associated with the reservation")
        public void the_hotel_is_associated_with_the_reservation() {
        }

        @When("the associate client calls POST /reviews with:")
        public void the_associate_client_calls_post_reviews_with(DataTable table) throws Exception {

                Map<String, String> data = table.asMap(String.class, String.class);

                Map<String, Object> payload = new HashMap<>();

                payload.put("reservationId", Integer.valueOf(data.get("reservationId")));
                payload.put("hotel", data.get("hotel"));
                payload.put("nameGuest", data.get("nameGuest"));
                payload.put("textualDescription", data.get("textualDescription"));
                payload.put("rating", Integer.valueOf(data.get("rating")));

                String body = objectMapper.writeValueAsString(payload);

                action = mvc.perform(
                                post("/reviews")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(body));
        }

        @Then("the associate client receives status code {int}")
        public void the_associate_client_receives_status_code(Integer statusCode) throws Exception {
                action.andExpect(status().is(statusCode));
        }

        @Then("the associate response contains reservationId {int}")
        public void the_associate_response_contains_reservationId(Integer id) throws Exception {
                action.andExpect(jsonPath("$.reservationId").value(id));
        }

        @Then("the associate response contains a hotel {string}")
        public void the_associate_response_contains_a_hotel(String hotel) throws Exception {
                action.andExpect(jsonPath("$.hotel").value(hotel));
        }

        @Then("the associate response contains guest called {string}")
        public void the_associate_response_contains_guest_called(String guest) throws Exception {
                action.andExpect(jsonPath("$.nameGuest").value(guest));
        }

        @Then("the associate response contains review with description {string}")
        public void the_associate_response_contains_review_with_description(String desc) throws Exception {
                action.andExpect(jsonPath("$.textualDescription").value(desc));
        }

        @Then("the associate response contains rating {int}")
        public void the_associate_response_contains_rating(Integer rating) throws Exception {
                action.andExpect(jsonPath("$.rating").value(rating));
        }
}