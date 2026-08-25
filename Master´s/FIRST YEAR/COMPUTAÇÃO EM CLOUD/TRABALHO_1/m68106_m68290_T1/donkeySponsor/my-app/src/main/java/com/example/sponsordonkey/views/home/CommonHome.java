package com.example.sponsordonkey.views.home;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.sponsordonkey.data.User.User;
import com.example.sponsordonkey.data.User.UserTypes;
import com.example.sponsordonkey.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
@AnonymousAllowed
public class CommonHome extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserService userService;

    public CommonHome() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName(LumoUtility.Gap.LARGE);

        H1 title = new H1("🐴 SponsorDonkey");
        title.getStyle().set("margin", "0");

        Paragraph subtitle = new Paragraph("Support endangered donkeys by connecting sponsors with local producers and following their daily lives.");
        subtitle.getStyle().set("color", "gray");

        Button loginButton = new Button("Login", e -> {
            UI.getCurrent().navigate("login");
        });

        loginButton.getStyle()
                .set("background-color", "#2563eb")
                .set("color", "white")
                .set("border-radius", "8px")
                .set("padding", "10px 20px")
                .set("font-weight", "bold");

        add(title, subtitle, loginButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null &&
            auth.isAuthenticated() &&
            !(auth instanceof AnonymousAuthenticationToken)) {

            String username = auth.getName();
            Optional<User> userOpt = userService.getByUsername(username);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                if (user.getUserType() == UserTypes.PRODUCER) {
                    event.forwardTo("homeP");
                } else if (user.getUserType() == UserTypes.SPONSOR) {
                    event.forwardTo("homeS");
                } else {
                    event.forwardTo("login");
                }
            }
        }
    }
}