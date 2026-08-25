package com.test.hotelbay.general;

import com.test.hotelbay.StepDefinition;

import io.cucumber.java.en.Given;

public class CommonStepDefinition extends StepDefinition {

    @Given("a valid administrator account")
    public void valid_administrator_account() {
    }

    @Given("a hotel with the name {string}")
    public void hotel_exists(String name) {
    }

    @Given("a room with the identifier {string}")
    public void room_exists(String id) {
    }

    @Given("a room with the number {int} available between {string} and {string}")
    public void room_available_between_dates(Integer roomNumber, String start, String end) {
    }

    @Given("the client gives valid information")
    public void valid_information() {
    }
}