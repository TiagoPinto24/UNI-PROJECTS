package com.test.hotelbay.reviews;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Map;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.Review.Review;
import com.test.hotelbay.Review.ReviewRepository;
import com.test.hotelbay.Reservation.Reservation;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.StepDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OneReviewStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private ReviewTestContext context;

        @Autowired
        private ReviewRepository reviewRepository;

        @Autowired
        private ReservationRepository reservationRepository;

        private Reservation reservation;

        @Given("the review service URL")
        public void the_review_service_url() {
        }

        @Given("a completed reservation exists")
        public void a_completed_reservation_exists() {

                reservation = new Reservation();

                reservation.setCheckin(LocalDate.now());
                reservation.setCheckout(LocalDate.now().plusDays(2));

                reservation = reservationRepository.saveAndFlush(reservation);
        }

        @Given("the reservation already has a review")
        public void the_reservation_already_has_a_review() {

                Review review = new Review(
                                reservation,
                                "existing review",
                                7);

                reviewRepository.saveAndFlush(review);
        }

        @When("the client calls POST /reviews with:")
        public void the_client_calls_post_reviews_with(DataTable table) throws Exception {

                Map<String, String> data = table.asMap(String.class, String.class);

                Map<String, Object> request = Map.of(
                                "reservationId", reservation.getId(),

                                "textualDescription",
                                data.get("textualDescription"),

                                "rating",
                                Integer.valueOf(data.get("rating")));

                String requestBody = objectMapper.writeValueAsString(request);

                context.setAction(
                                mvc.perform(
                                                post("/reviews")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(requestBody)));
        }

        @Then("the client receives status code {int}")
        public void the_client_receives_status_code(Integer statusCode)
                        throws Exception {

                context.getAction()
                                .andExpect(status().is(statusCode));
        }

        @Then("the response contains message {string}")
        public void the_response_contains_message(String message)
                        throws Exception {

                context.getAction()
                                .andExpect(
                                                content().string(
                                                                Matchers.containsString(message)));
        }

        @Then("the review response contains:")
        public void the_review_response_contains(DataTable table)
                        throws Exception {

                Map<String, String> expected = table.asMap(String.class, String.class);

                for (Map.Entry<String, String> entry : expected.entrySet()) {

                        String key = entry.getKey();
                        String value = entry.getValue();

                        try {

                                int number = Integer.parseInt(value);

                                context.getAction()
                                                .andExpect(
                                                                jsonPath("$." + key)
                                                                                .value(number));

                        } catch (NumberFormatException e) {

                                context.getAction()
                                                .andExpect(
                                                                jsonPath("$." + key)
                                                                                .value(value));
                        }
                }
        }
}