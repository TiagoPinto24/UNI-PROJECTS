package com.example.sponsordonkey.views.register;

import com.example.sponsordonkey.data.Role;
import com.example.sponsordonkey.data.User.User;
import com.example.sponsordonkey.data.User.UserTypes;
import com.example.sponsordonkey.services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Register")
@Route("register")
@AnonymousAllowed
public class RegisterView extends Composite<VerticalLayout> {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterView() {
        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        ComboBox<SampleItem> comboBox = new ComboBox<>();

        Button buttonPrimary = new Button("Sign in", e -> {
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (!password.equals(confirmPassword)) {
                Notification.show("Passwords do not match!");
                return;
            }

            String usernameValue = usernameField.getValue();
            String emailValue = emailField.getValue();
            String passwordValue = passwordEncoder.encode(password);

            Set<Role> rolesValue = new HashSet<>();
            rolesValue.add(Role.USER);

            UserTypes userType = null;
            SampleItem selected = comboBox.getValue();
            if (selected != null) {
                switch (selected.value()) {
                    case "first" -> userType = UserTypes.PRODUCER;
                    case "second" -> userType = UserTypes.SPONSOR;
                }
            }

            User newUser = new User();
            newUser.setUsername(usernameValue.trim());
            newUser.setEmail(emailValue);
            newUser.setHashedPassword(passwordValue);
            newUser.setRoles(rolesValue);
            newUser.setUserType(userType);

            userService.save(newUser);
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        Button buttonSecondary = new Button("Back", e -> {
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);

        usernameField.setLabel("Username");
        emailField.setLabel("Email");
        passwordField.setLabel("Password");
        confirmPasswordField.setLabel("Confirm Password");

        comboBox.setLabel("User Type");
        setComboBoxSampleData(comboBox);

        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.setEnabled(false);

        usernameField.addValueChangeListener(e ->
                validateFields(usernameField, emailField, passwordField, confirmPasswordField, comboBox, buttonPrimary)
        );

        emailField.addValueChangeListener(e ->
                validateFields(usernameField, emailField, passwordField, confirmPasswordField, comboBox, buttonPrimary)
        );

        passwordField.addValueChangeListener(e ->
                validateFields(usernameField, emailField, passwordField, confirmPasswordField, comboBox, buttonPrimary)
        );

        confirmPasswordField.addValueChangeListener(e ->
                validateFields(usernameField, emailField, passwordField, confirmPasswordField, comboBox, buttonPrimary)
        );

        comboBox.addValueChangeListener(e ->
                validateFields(usernameField, emailField, passwordField, confirmPasswordField, comboBox, buttonPrimary)
        );

        getContent().add(
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                comboBox,
                buttonPrimary,
                buttonSecondary
        );
    }

    private void validateFields(TextField usernameField,
                                TextField emailField,
                                PasswordField passwordField,
                                PasswordField confirmPasswordField,
                                ComboBox<SampleItem> comboBox,
                                Button button) {

        boolean passwordsMatch =
                passwordField.getValue() != null &&
                passwordField.getValue().equals(confirmPasswordField.getValue());

        boolean allFilled =
                !usernameField.isEmpty() &&
                !emailField.isEmpty() &&
                !passwordField.isEmpty() &&
                !confirmPasswordField.isEmpty() &&
                comboBox.getValue() != null &&
                passwordsMatch;

        button.setEnabled(allFilled);
    }

    record SampleItem(String value, String label, Boolean disabled) {}

    private void setComboBoxSampleData(ComboBox<SampleItem> comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "Producer", null));
        sampleItems.add(new SampleItem("second", "Sponsor", null));

        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(SampleItem::label);
    }
}