package com.test.hotelbay.reviews;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

public class SubmitReviewStepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        private ResultActions action;

        @Given("an existing completed reservation with no review")
        public void a_completed_reservation_exists() {
        }

        @When("the submit client calls POST /reviews with:")
        public void the_submit_client_calls_post_reviews_with(DataTable table) throws Exception {

                Map<String, String> data = table.asMap(String.class, String.class);

                Map<String, Object> request = Map.of(
                                "reservationId", Integer.valueOf(data.get("reservationId")),
                                "textualDescription", data.get("textualDescription"),
                                "rating", Integer.valueOf(data.get("rating")));

                String body = objectMapper.writeValueAsString(request);

                action = mvc.perform(
                                post("/reviews")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(body));
        }

        @Then("the submit client receives status code {int}")
        public void the_submit_client_receives_status_code(Integer statusCode) throws Exception {
                action.andExpect(status().is(statusCode));
        }

        @Then("the submit response contains review with description {string}")
        public void the_submit_response_contains_review_with_description(String description) throws Exception {
                action.andExpect(jsonPath("$.textualDescription").value(description));
        }

        @Then("the submit response contains rating {int}")
        public void the_submit_response_contains_rating(Integer rating) throws Exception {
                action.andExpect(jsonPath("$.rating").value(rating));
        }
}