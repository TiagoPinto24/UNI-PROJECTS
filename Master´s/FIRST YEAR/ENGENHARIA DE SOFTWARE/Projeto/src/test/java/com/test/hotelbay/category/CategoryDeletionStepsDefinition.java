package com.test.hotelbay.category;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.test.hotelbay.ScenarioContext;
import com.test.hotelbay.StepDefinition;
import com.test.hotelbay.Account.Account;
import com.test.hotelbay.Account.AccountType;
import com.test.hotelbay.Category.Category;
import com.test.hotelbay.Category.CategoryRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryDeletionStepsDefinition extends StepDefinition{
    private final ScenarioContext context;

    public CategoryDeletionStepsDefinition(ScenarioContext context) {
        this.context = context;
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    String requestBody;

    Account existingAdminAccount = new Account("admin@email.com", "Admin Lopez", "AdminLopez456", "Password123", AccountType.ADMIN);
    Category existingCategory = new Category(existingAdminAccount, "Suíte");

    @Transactional
    @Given("a category with the name Suíte belongs to the existing admin")
    public void and_a_category_with_the_name_suite_belongs_to_grand_hotel() {
        existingCategory.setAdmin(context.getExistingAdminAccount());
        if (categoryRepository.findByNameAndAdmin(existingCategory.getName(), context.getExistingAdminAccount()).isEmpty()) {
            categoryRepository.save(existingCategory);
        } else {
            categoryRepository.deleteByNameAndAdmin(existingCategory.getName(),context.getExistingAdminAccount());
            categoryRepository.save(existingCategory);
        }
    }

    @When("the client calls category\\/delete")
    public void when_the_client_calls_hotel_category_delete() throws Exception {
        String adminId = String.valueOf(context.getExistingAdminAccount().getId());
        String categoryId = String.valueOf(existingCategory.getId());
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n" + adminId + "\n\n\n\n\n\n\n\n\n\n\n" + categoryId);

        ResultActions action = mvc.perform(delete(context.getUrl() + "/category/delete/" + categoryId)
            .header("X-ADMIN-ID", adminId)
            .contentType(MediaType.APPLICATION_JSON));
        context.setResultActions(action);
    }

    @Then("the category is deleted from the system")
    public void and_the_category_is_deleted_from_the_system() {
        Optional<Category> categoryOptional = categoryRepository.findByNameAndAdmin(existingCategory.getName(),context.getExistingAdminAccount());
        assertFalse(categoryOptional.isPresent());
    }
}
