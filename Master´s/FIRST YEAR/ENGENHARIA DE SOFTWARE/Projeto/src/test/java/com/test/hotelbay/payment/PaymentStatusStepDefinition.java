package com.test.hotelbay.payment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PaymentStatusStepDefinition extends StepDefinition {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AccountRepository accountRepository;

    private ResultActions response;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long reservationId;
    private Long paymentId;

    @Given("the payment status service URL")
    public void thePaymentStatusServiceUrl() {
    }

    @Given("a payment status reservation with id {int} exists with status {string}")
    public void createReservation(int id, String status) throws Exception {

        Room room = roomRepository.save(new Room());
        Account account = accountRepository.save(new Account());

        Map<String, Object> request = Map.of(
                "room", room.getId(),
                "guest", account.getId(),
                "checkInDate", "2026-06-20",
                "checkOutDate", "2026-06-22",
                "numberGuests", 1);

        String result = mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(result);

        reservationId = json.get("id").asLong();
    }

    @Given("a payment with id {int} exists for reservation {int} with status {string}")
    public void createPayment(int paymentIdInput, int reservationIdInput, String status) throws Exception {

        Map<String, Object> request = Map.of(
                "reservationId", reservationId);

        String result = mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(result);

        paymentId = json.get("paymentId").asLong();
    }

    @When("the client updates payment {int} to {string}")
    public void patchPayment(int id, String status) throws Exception {

        Map<String, Object> request = Map.of(
                "status", status);

        response = mockMvc.perform(
                patch("/payments/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
    }

    @Then("the status payment client receives status code {int}")
    public void statusCode(int code) throws Exception {
        response.andExpect(status().is(code));
    }

    @Then("the status payment response contains payment status {string}")
    public void paymentStatus(String status) throws Exception {
        response.andExpect(jsonPath("$.paymentStatus").value(status));
    }

    @Then("the reservation with id {int} has status {string}")
    public void reservationStatus(int id, String status) throws Exception {
        response.andExpect(jsonPath("$.reservationStatus").value(status));
    }
}