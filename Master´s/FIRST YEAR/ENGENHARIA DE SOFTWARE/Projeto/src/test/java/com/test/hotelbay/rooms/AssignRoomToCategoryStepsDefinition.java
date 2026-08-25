package com.test.hotelbay.rooms;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;
import com.test.hotelbay.Room.Room;
import com.test.hotelbay.Room.RoomRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AssignRoomToCategoryStepsDefinition extends StepDefinition {
    private final ScenarioContext context;

    public AssignRoomToCategoryStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    String requestBody;

    @Autowired
    private CategoryRepository categoryRepository;


    Account existingAdminAccount = new Account("admin@email.com", "Admin Lopez", "AdminLopez456", "Password123", AccountType.ADMIN);
    Category existingCategory = new Category(existingAdminAccount, "Suíte");

    @Transactional
    @Given("a category with the name Suíte belonging to the existing admin")
    public void a_category_with_the_name_suíte_belonging_to_the_existing_admin() {
        existingCategory.setAdmin(context.getExistingAdminAccount());
        if (categoryRepository.findByNameAndAdmin(existingCategory.getName(), context.getExistingAdminAccount()).isEmpty()) {
            categoryRepository.save(existingCategory);
        } else {
            categoryRepository.deleteByNameAndAdmin(existingCategory.getName(),context.getExistingAdminAccount());
            categoryRepository.save(existingCategory);
        }
    }

    @When("the client gives the category in the body of the request")
    public void the_client_gives_the_category_in_the_body_of_the_request() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        List<Category> updatedList = context.getExistingRoom().getCategories();
        updatedList.add(existingCategory);
        payload.put("categories", updatedList);

        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("the client calls room\\/changeInfo")
    public void the_client_calls_room_change_info() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String hotelId = String.valueOf(context.getExistingHotel().getId());
        String roomId = String.valueOf(context.getExistingRoom().getId());

        ResultActions action = mvc.perform(put(context.getUrl() + "/room/changeInfo/" + roomId)
            .header("X-ADMIN-ID", adminId)
            .header("X-HOTEL-ID", hotelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }

    @Transactional
    @Then("the room 121 should have the category named Suíte")
    public void the_room_should_have_the_category_named_suíte() {
        Optional<Room> roomOptional = roomRepository.findByNumberAndHotel("121", context.getExistingHotel());
        assertTrue(roomOptional.isPresent());

        Room room = roomOptional.get();
        assertTrue(room.getCategories().stream().map(Category::getId).toList().contains(existingCategory.getId()));
    }

}
