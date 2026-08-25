package com.test.hotelbay.general;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Hotel.Hotel;
import com.test.hotelbay.Hotel.HotelRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchGeneralStepsDefinition extends StepDefinition{

    @Autowired
    private MockMvc mvc;

    private final ScenarioContext context;

    public SearchGeneralStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private HotelRepository hotelRepository;

    
    Hotel existingHotel = new Hotel("Grand Hotel", "A 4 star hotel situated in the middle of Lisbon", "Lisbon", "grandhotel@email.com", 
        new ArrayList<>(List.of("spa", "pool", "restaurant")), new ArrayList<>(List.of()), new ArrayList<>(List.of()), new ArrayList<>());
    
    @Transactional
    @Given("a hotel with the name Grand Hotel")
    public void and_a_hotel_with_the_name_grand_hotel() {
        if (hotelRepository.findByName(existingHotel.getName()).isEmpty()) {
            hotelRepository.save(existingHotel);
        } else {
            hotelRepository.deleteByName("Grand Hotel");
            hotelRepository.save(existingHotel);
        }
        context.setExistingHotel(existingHotel);
    }


    @When("the client calls search rooms with location Lisbon and checkin {int}\\/{int}\\/{int} and checkout {int}\\/{int}\\/{int}")
    public void call_search_rooms(Integer d1, Integer m1, Integer y1, Integer d2, Integer m2, Integer y2) throws Exception {
        String userId = String.valueOf(context.getExistingAccount().getId());

        ResultActions action = mvc.perform(get(context.getUrl() + "/search/rooms")
        .header("X-USER-ID", userId)
        .param("location", "Lisbon")
        .param("checkin", String.format("%04d-%02d-%02d", y1, m1, d1))
        .param("checkout", String.format("%04d-%02d-%02d", y2, m2, d2))
        .contentType(MediaType.APPLICATION_JSON));

        context.setResultActions(action);
    }


    @Then("the response should contain at least the room with the number 121")
    public void the_response_should_contain_the_information_that_this_room_is_not_available_in_this_time_period() throws Exception {
        context.getResultActions().andExpect(jsonPath("$.rooms[*].number", hasItem("121")));
    }
}
