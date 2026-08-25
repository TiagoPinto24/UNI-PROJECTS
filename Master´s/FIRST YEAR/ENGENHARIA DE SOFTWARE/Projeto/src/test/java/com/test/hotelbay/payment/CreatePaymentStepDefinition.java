package com.test.hotelbay.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Map;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Reservation.Reservation;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Reservation.ReservationStatus;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class CreatePaymentStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ReservationRepository reservationRepository;

        private ResultActions action;
        private String endpoint;
        private Long reservationId;

        @Given("the create payment service URL")
        public void the_create_payment_service_url() {
                endpoint = "/payments";
        }

        @Given("a payment reservation exists")
        public void a_payment_reservation_exists() {

                Reservation reservation = new Reservation();
                reservation.setStatus(ReservationStatus.PENDING);

                reservation = reservationRepository.save(reservation);

                reservationId = reservation.getId();
        }

        @When("the create payment client calls POST payments with:")
        public void the_create_payment_client_calls_post(DataTable table) throws Exception {

                Map<String, String> data = table.asMap(String.class, String.class);

                String body = "{"
                                + "\"reservationId\":" + reservationId + ","
                                + "\"amount\":" + data.get("amount") + ","
                                + "\"paymentDate\":\"" + data.get("paymentDate") + "\""
                                + "}";

                action = mvc.perform(
                                post(endpoint)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(body));
        }

        @Then("the create payment client receives status code {int}")
        public void the_create_payment_client_receives_status_code(Integer code) throws Exception {
                action.andExpect(status().is(code));
        }

        @Then("the create payment response contains payment status {string}")
        public void the_create_payment_response_contains_payment_status(String statusValue) throws Exception {
                action.andExpect(jsonPath("$.paymentStatus", Matchers.is(statusValue)));
        }
}