package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.StepDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class ModifyReservationStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions action;

    private String checkIn;
    private String checkOut;

    private Integer reservationId;
    private String baseUrl;

    @Given("the modify reservation service URL")
    public void the_modify_reservation_service_url() {
        this.baseUrl = "/reservations";
    }

    @Given("an existing modify reservation with id {int}")
    public void an_existing_modify_reservation_with_id(Integer id) {

        this.reservationId = id;

    }

    @Given("the client wants to change the dates to {string} and {string}")
    public void the_client_wants_to_change_the_dates(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    @When("^the client calls PUT /reservations/(\\d+) with:$")
    public void the_client_updates_the_reservation_dates(Integer id, DataTable table) throws Exception {

        this.reservationId = id;

        Map<String, String> data = table.asMap(String.class, String.class);

        checkIn = data.get("checkIn");
        checkOut = data.get("checkOut");

        Map<String, String> body = Map.of(
                "startDate", checkIn,
                "endDate", checkOut);

        String requestBody = objectMapper.writeValueAsString(body);

        action = mvc.perform(put(baseUrl + "/" + reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Then("the modify reservation client receives status code {int}")
    public void the_modify_reservation_client_receives_status_code(
            Integer statusCode) throws Exception {

        action.andExpect(
                status().is(statusCode));
    }

    @Then("the reservation is updated with the new dates")
    public void the_reservation_is_updated_with_the_new_dates() throws Exception {
        action.andExpect(jsonPath("$.startDate").value(checkIn))
                .andExpect(jsonPath("$.endDate").value(checkOut));
    }
}