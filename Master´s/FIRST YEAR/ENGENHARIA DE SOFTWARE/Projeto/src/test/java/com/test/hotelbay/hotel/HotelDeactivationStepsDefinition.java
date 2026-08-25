package com.test.hotelbay.hotel;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HotelDeactivationStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public HotelDeactivationStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    String requestBody;

    
    @Autowired
    private HotelRepository hotelRepository;

    @When("the information given is valid to alter the hotel activation state")
    public void the_information_given_is_valid_to_alter_the_hotel_activation_state() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("active", false);

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("client calls \\/hotel\\/changeInfo")
    public void and_calls_hotel_changeInfo() throws Exception{
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());

        ResultActions action = mvc.perform(put(context.getUrl() + "/hotel/changeInfo/" + hotelId)
            .header("X-ADMIN-ID", adminId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }



    @Then("the hotel should be deactivated")
    public void the_hotel_should_be_deactivated() {
        Optional<Hotel> hotelOptional = hotelRepository.findByName(context.getExistingHotel().getName());
        assertTrue(hotelOptional.isPresent());

        
        Hotel hotel = hotelOptional.get();
        assertFalse(hotel.getActive());   
    }
}
