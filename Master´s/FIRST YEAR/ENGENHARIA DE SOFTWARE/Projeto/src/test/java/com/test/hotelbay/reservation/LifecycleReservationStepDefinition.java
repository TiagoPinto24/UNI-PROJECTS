package com.test.hotelbay.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.StepDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LifecycleReservationStepDefinition extends StepDefinition {

    @Autowired
    private MockMvc mvc;

    private ResultActions action;

    private String baseUrl;

    @Given("the lifecycle reservation service URL")
    public void the_lifecycle_reservation_service_url() {

        baseUrl = "/reservations";

    }

    @When("the client calls reservations with statuses")
    public void the_client_calls_reservation_statuses() throws Exception {
        action = mvc.perform(get(baseUrl + "/statuses")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the lifecycle reservation client receives status code {int}")
    public void the_lifecycle_reservation_client_receives_status_code(
            Integer statusCode) throws Exception {

        action.andExpect(
                status().is(statusCode));
    }

    @Then("the response contains:")
    public void the_response_contains(DataTable table) throws Exception {
        List<String> expectedStatuses = table.asList();

        for (int i = 0; i < expectedStatuses.size(); i++) {
            action.andExpect(jsonPath("$[" + i + "]")
                    .value(expectedStatuses.get(i)));
        }
    }
}