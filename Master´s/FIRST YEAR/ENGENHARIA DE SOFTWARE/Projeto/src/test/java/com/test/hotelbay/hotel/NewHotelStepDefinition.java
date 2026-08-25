package com.test.hotelbay.hotel;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

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

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NewHotelStepDefinition extends StepDefinition{
    private final ScenarioContext context;

    public NewHotelStepDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HotelRepository hotelRepository;

    String requestBody;

    @Transactional
    @Given("there is no hotel with the name Grand Hotel")
    public void and_there_is_no_hotel_with_the_name_grand_hotel() {
        hotelRepository.deleteByName("Grand Hotel");
    }

    @When("the client gives valid information for a hotel creation")
    public void when_the_client_gives_valid_information_for_a_hotel_creation() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Grand Hotel");
        payload.put("description", "A 4 star hotel situated in the middle of Lisbon");
        payload.put("location", "Lisbon");
        payload.put("contact", "grandhotel@email.com");
        payload.put("comodities", new ArrayList<>(List.of("spa", "pool", "restaurant")));
        payload.put("adminList", new ArrayList<>(List.of(context.getExistingAdminAccount())));
        
        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls \\/hotel\\/create")
    public void and_calls_hotel_create() throws Exception {
        ResultActions action = mvc.perform(post(context.getUrl() + "/hotel/create")
                .header("X-ADMIN-ID", String.valueOf(context.getExistingAdminAccount().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        context.setResultActions(action);
    }

    @Then("a new hotel is created")
    public void and_a_new_hotel_is_created() throws Exception{
        Optional<Hotel> hotelOptional = hotelRepository.findByName("Grand Hotel");
        assertTrue(hotelOptional.isPresent());

        Hotel hotel = hotelOptional.get();
        assertEquals("Grand Hotel", hotel.getName());
        assertEquals("A 4 star hotel situated in the middle of Lisbon", hotel.getDescription());
        assertEquals("Lisbon", hotel.getLocation());
        assertEquals("grandhotel@email.com", hotel.getContact());
    }
}
