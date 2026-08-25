package com.test.hotelbay.reviews;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateReviewStepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        private ResultActions action;

        private String reservationServiceUrl;

        private final Map<Integer, Integer> reservations = new HashMap<>();

        private int expectedRating;

        @Given("the review reservation service URL")
        public void the_review_reservation_service_url() {
                this.reservationServiceUrl = "http://localhost:8080";
        }

        @Given("an existing review reservation with id {int}")
        public void an_existing_review_reservation_with_id(Integer id) {

                reservations.put(id, 5);

        }

        @Given("the client wants to change the rate to {string}")
        public void the_client_wants_to_change_the_rate_to(
                        String rating) {

                this.expectedRating = Integer.parseInt(rating);

        }

        @When("^the client calls PUT /reviews/(\\d+) with:$")
        public void the_client_calls_put_reviews_with(
                        Integer id,
                        DataTable table) throws Exception {

                Map<String, String> data = table.asMap(
                                String.class,
                                String.class);

                expectedRating = Integer.parseInt(
                                data.get("rating"));

                Map<String, Object> request = Map.of(
                                "rating",
                                expectedRating);

                String requestBody = objectMapper.writeValueAsString(request);

                action = mvc.perform(

                                put("/reviews/{id}", id)

                                                .contentType(MediaType.APPLICATION_JSON)

                                                .content(requestBody)

                );

        }

        @Then("the update client receives status code {int}")
        public void the_update_client_receives_status_code(
                        Integer statusCode) throws Exception {

                action.andExpect(
                                status().is(statusCode));

        }

        @Then("the rating is updated with the score")
        public void the_rating_is_updated_with_the_score()
                        throws Exception {

                action.andExpect(
                                jsonPath("$.rating")
                                                .value(expectedRating));

        }

}