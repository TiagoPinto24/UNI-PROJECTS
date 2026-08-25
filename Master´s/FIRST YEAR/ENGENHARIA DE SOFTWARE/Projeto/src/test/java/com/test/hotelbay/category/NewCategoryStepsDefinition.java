package com.test.hotelbay.category;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NewCategoryStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public NewCategoryStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    String requestBody;

    @Autowired
    private CategoryRepository categoryRepository;

    @When("the client gives valid category information")
    public void when_the_client_gives_valid_category_informations() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Suíte");
        payload.put("subCategories", new ArrayList<>(List.of()));
        
        requestBody = objectMapper.writeValueAsString(payload);
    }

    @When("calls category\\/create")
    public void and_calls_hotel_category_create() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());

        ResultActions action = mvc.perform(post(context.getUrl() + "/category/create")
            .header("X-ADMIN-ID", adminId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody));
        context.setResultActions(action);
    }

    @Then("a new category is created")
    public void and_a_new_room_is_created() throws Exception{
        Optional<Category> categoryOptional = categoryRepository.findByNameAndAdmin("Suíte",context.getExistingAdminAccount());
        assertTrue(categoryOptional.isPresent());

        Category category = categoryOptional.get();
        assertEquals("Suíte", category.getName());
        assertEquals(context.getExistingAdminAccount().getId(), category.getAdmin().getId());
    }
}
