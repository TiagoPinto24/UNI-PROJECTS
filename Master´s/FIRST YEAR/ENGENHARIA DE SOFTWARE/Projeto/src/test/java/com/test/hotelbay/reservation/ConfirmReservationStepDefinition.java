package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Payment.Payment;
import com.test.hotelbay.Payment.PaymentRepository;
import com.test.hotelbay.Reservation.Reservation;
import com.test.hotelbay.Reservation.ReservationRepository;
import com.test.hotelbay.Reservation.ReservationStatus;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConfirmReservationStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions action;

    private Long paymentId;

    @Given("the payment service URL")
    public void the_payment_service_url() {
    }

    @Given("a payment exists for a reservation with id {long}")
    public void a_payment_exists_for_a_reservation_with_id(Long reservationId) {

        Reservation reservation = new Reservation(null, null, null, null, null);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation = reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setStatus("PENDING");

        payment = paymentRepository.save(payment);

        this.paymentId = payment.getId();
    }

    @When("the client calls PATCH /payments with id {int} and status {string}")
    public void the_client_calls_patch(Integer ignoredId, String paymentStatus) throws Exception {

        Map<String, Object> payload = new HashMap<>();
        payload.put("status", paymentStatus);

        String requestBody = objectMapper.writeValueAsString(payload);

        action = mvc.perform(
                patch("/payments/" + this.paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));
    }

    @Then("the confirm reservation client receives status code {int}")
    public void the_confirm_reservation_client_receives_status_code(Integer statusCode) throws Exception {

        action.andExpect(status().is(statusCode));
    }

    @Then("the response contains payment status {string}")
    public void the_response_contains_payment_status(String status) throws Exception {

        action.andExpect(jsonPath("$.paymentStatus", Matchers.is(status)));
    }

    @Then("the reservation status becomes {string}")
    public void the_reservation_status_becomes(String status) throws Exception {

        action.andExpect(jsonPath("$.reservationStatus", Matchers.is(status)));
    }
}