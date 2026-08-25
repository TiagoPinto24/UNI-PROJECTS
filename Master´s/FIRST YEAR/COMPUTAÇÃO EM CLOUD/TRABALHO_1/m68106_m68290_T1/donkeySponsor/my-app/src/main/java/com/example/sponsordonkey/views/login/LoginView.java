package com.example.sponsordonkey.views.login;

import com.example.sponsordonkey.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {


    public LoginView(AuthenticatedUser authenticatedUser) {
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Welcome!");
        i18n.getHeader().setDescription("Enter your credentials below.");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);


        setForgotPasswordButtonVisible(false);
        setOpened(true);

        Button registerButton = new Button("Register", e -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });
        registerButton.setWidthFull();
        getFooter().add(registerButton);
        
    }
}