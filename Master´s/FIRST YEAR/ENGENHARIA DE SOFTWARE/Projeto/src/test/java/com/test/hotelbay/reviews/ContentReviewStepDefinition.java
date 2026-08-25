package com.test.hotelbay.reviews;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ContentReviewStepDefinition {

    @Autowired
    private ReviewTestContext context;

    @Then("the content review is created successfully")
    public void the_content_review_is_created_successfully() throws Exception {

        context.getAction()
                .andExpect(jsonPath("$.rating").exists());

        context.getAction()
                .andExpect(jsonPath("$.textualDescription").exists());
    }
}