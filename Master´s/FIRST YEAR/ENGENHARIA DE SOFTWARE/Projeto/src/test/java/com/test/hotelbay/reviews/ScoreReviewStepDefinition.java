package com.test.hotelbay.reviews;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class ScoreReviewStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        private ResultActions action;

        private boolean reservationCompleted;

        private boolean clientAssociated;

        @Given("a completed reservation exists for score review")
        public void a_completed_reservation_exists_for_score_review() {

                reservationCompleted = true;

        }

        @Given("the client is associated with the reservation for score review")
        public void the_client_is_associated_with_the_reservation_for_score_review() {

                clientAssociated = true;

        }

        @When("the score client calls POST /reviews with:")
        public void the_score_client_calls_post_reviews_with(
                        DataTable table) throws Exception {

                Map<String, String> data = table.asMap(
                                String.class,
                                String.class);

                Map<String, Object> request = Map.of(

                                "rating",
                                Integer.valueOf(
                                                data.get("rating")),

                                "textualDescription",
                                data.get("description")

                );

                String body = objectMapper.writeValueAsString(request);

                action = mvc.perform(

                                post("/reviews")

                                                .contentType(MediaType.APPLICATION_JSON)

                                                .content(body)

                );

        }

        @Then("the score client receives status code {int}")
        public void the_score_client_receives_status_code(
                        Integer statusCode) throws Exception {

                action.andExpect(
                                status().is(statusCode));

        }

        @Then("the score response contains:")
        public void the_score_response_contains(
                        DataTable table) throws Exception {

                Map<String, String> expected = table.asMap(
                                String.class,
                                String.class);

                for (Map.Entry<String, String> entry : expected.entrySet()) {

                        String key = entry.getKey();

                        String value = entry.getValue();

                        try {

                                int number = Integer.parseInt(value);

                                action.andExpect(
                                                jsonPath("$." + key)
                                                                .value(number));

                        } catch (NumberFormatException e) {

                                action.andExpect(
                                                jsonPath("$." + key)
                                                                .value(value));

                        }

                }

        }

}