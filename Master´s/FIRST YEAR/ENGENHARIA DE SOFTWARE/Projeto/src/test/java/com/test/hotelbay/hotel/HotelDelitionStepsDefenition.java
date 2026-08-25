package com.test.hotelbay.hotel;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HotelDelitionStepsDefenition extends StepDefinition{
    private final ScenarioContext context;

    public HotelDelitionStepsDefenition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private HotelRepository hotelRepository;

    String requestBody;

    @When("the client calls \\/hotel\\/delete")
    public void when_the_client_calls_hotel_delete() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());

        ResultActions action = mvc.perform(delete(context.getUrl() + "/hotel/delete/" + hotelId)
            .header("X-ADMIN-ID", adminId)
            .contentType(MediaType.APPLICATION_JSON));
        context.setResultActions(action);
    }

    @Then("the hotel is deleted from the system")
    public void and_the_hotel_is_deleted_from_the_system() {
        Optional<Hotel> hotelOptional = hotelRepository.findByName(context.getExistingHotel().getName());
        assertFalse(hotelOptional.isPresent());
    }

}
