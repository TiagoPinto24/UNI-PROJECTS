package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CancelReservationStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    ResultActions action;

    private String baseUrl;
    private Integer reservationId;

    @Given("the cancel reservation service URL")
    public void the_cancel_reservation_service_url() {

        baseUrl = "/reservations";

    }

    @Given("an existing reservation with id {int}")
    public void an_existing_reservation_with_id(Integer id) {
        reservationId = id;

    }

    @When("^the client calls PATCH /reservations/(\\d+) with status \"([^\"]*)\"$")
    public void the_client_calls_reservations(
            Integer id,
            String status) throws Exception {

        String requestBody = "{ \"status\": \"" + status + "\" }";

        action = mvc.perform(
                patch(baseUrl + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));
    }

    @Then("the cancel reservation client receives status code {int}")
    public void the_cancel_reservation_client_receives_status_code(
            Integer statusCode) throws Exception {

        action.andExpect(
                status().is(statusCode));
    }

    @Then("the reservation status is updated to {string}")
    public void the_reservation_is_updated_with_status(String status) throws Exception {
        action.andExpect(jsonPath("$.status", Matchers.is(status)));
    }
}