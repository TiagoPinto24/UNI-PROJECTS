package com.test.hotelbay.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FailedPaymentStepDefinition extends StepDefinition {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private ResultActions response;

        private Long reservationId;
        private Long paymentId;

        @Given("the failed payment service URL")
        public void theFailedPaymentServiceURL() {
        }

        @Given("a failed payment reservation with id {int} exists with status {string}")
        public void aFailedPaymentReservationExists(int id, String status) throws Exception {

                Map<String, Object> request = Map.of(
                                "id", id,
                                "status", status);

                ResultActions result = mockMvc.perform(post("/test/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                String body = result.andReturn()
                                .getResponse()
                                .getContentAsString();

                reservationId = objectMapper.readTree(body)
                                .get("id")
                                .asLong();
        }

        @Given("a payment with transaction number {int} exists with status {string}")
        public void aPaymentWithTransactionNumberExists(int number, String status) throws Exception {

                Map<String, Object> payload = Map.of(
                                "reservationId", reservationId);

                ResultActions result = mockMvc.perform(post("/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                                .andExpect(status().isOk());

                String body = result.andReturn()
                                .getResponse()
                                .getContentAsString();

                paymentId = objectMapper.readTree(body)
                                .get("paymentId")
                                .asLong();
        }

        @When("the client updates payment status to {string} for payment {long}")
        public void callPatchPayment(String status, Long ignoredPaymentId) throws Exception {

                Map<String, String> request = Map.of("status", status);

                response = mockMvc.perform(
                                patch("/payments/" + this.paymentId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)));
        }

        @Then("the failed payment client receives status code {int}")
        public void failedStatus(int code) throws Exception {
                response.andExpect(status().is(code));
        }

        @Then("the failed payment response contains payment status {string}")
        public void failedPaymentStatus(String status) throws Exception {
                response.andExpect(jsonPath("$.paymentStatus").value(status));
        }

        @Then("the reservation status remains {string}")
        public void reservationStatusRemains(String status) throws Exception {
                response.andExpect(jsonPath("$.reservationStatus").value(status));
        }
}