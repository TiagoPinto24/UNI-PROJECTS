package com.example.sponsordonkey.views.home.homeSponsor;

import java.util.List;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.example.sponsordonkey.data.Post.Post;
import com.example.sponsordonkey.services.PostService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.component.notification.Notification;

import jakarta.annotation.security.PermitAll;

@PageTitle("Home")
@Route("homeS")
@PermitAll
public class HomeSponsorView extends Composite<VerticalLayout> {

    private final PostService service;

    private List<Post> posts;
    private int currentIndex = 0;
    private int page = 0;

    private final VerticalLayout postContainer = new VerticalLayout();

    public HomeSponsorView(PostService service) {

        this.service = service;

        buildUI();
        loadPosts();
    }

    private void buildUI() {

        VerticalLayout root = getContent();
        root.setSizeFull();

        Button left = new Button("⬅");
        left.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button right = new Button("➡");
        right.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        left.addClickListener(e -> {
            if (posts != null && !posts.isEmpty()) {
                currentIndex = (currentIndex - 1 + posts.size()) % posts.size();
                showPost();
            }
        });

        right.addClickListener(e -> {
            if (posts != null && !posts.isEmpty()) {
                currentIndex = (currentIndex + 1) % posts.size();
                showPost();
            }
        });

        HorizontalLayout arrowsRow = new HorizontalLayout(left, right);
        arrowsRow.setWidthFull();
        arrowsRow.setAlignItems(FlexComponent.Alignment.CENTER);
        arrowsRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        arrowsRow.getStyle()
                .set("position", "fixed")
                .set("top", "50%")
                .set("left", "0")
                .set("transform", "translateY(-50%)")
                .set("padding", "0 20px")
                .set("box-sizing", "border-box")
                .set("z-index", "1000");

        Button logout = new Button("Logout", e -> {

            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

            logoutHandler.logout(
                    VaadinServletRequest.getCurrent().getHttpServletRequest(),
                    null,
                    null);

            getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
        });

        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button donate = new Button("Donate");
        donate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        donate.addClickListener(e -> {

            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Make a donation");

            NumberField amountField = new NumberField("Amount (€)");
            amountField.setMin(1);
            amountField.setStep(1);
            amountField.setPlaceholder("Ex: 10");

            Button confirm = new Button("Confirm", event -> {
                Double amount = amountField.getValue();

                if (amount == null || amount <= 0) {
                    showNotification("Enter a valid value!");
                    return;
                }

                showNotification("Donation of " + amount + "€ successfully completed!");

                dialog.close();
            });

            confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            VerticalLayout dialogLayout = new VerticalLayout(amountField, confirm);
            dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            dialog.add(dialogLayout);
            dialog.open();
        });

        HorizontalLayout bottomRow = new HorizontalLayout();
        bottomRow.setWidthFull();

        HorizontalLayout leftBox = new HorizontalLayout(logout);
        HorizontalLayout centerBox = new HorizontalLayout(donate);
        HorizontalLayout rightBox = new HorizontalLayout();

        leftBox.setWidthFull();
        centerBox.setWidthFull();
        rightBox.setWidthFull();

        leftBox.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        centerBox.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rightBox.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        bottomRow.add(leftBox, centerBox, rightBox);

        Div topSpacer = new Div();
        Div bottomSpacer = new Div();

        postContainer.setAlignItems(FlexComponent.Alignment.CENTER);

        root.add(topSpacer, postContainer, bottomSpacer, bottomRow, arrowsRow);
        root.expand(topSpacer, bottomSpacer);
    }

    private void showNotification(String message) {
        Notification notification = Notification.show(message);
        notification.setPosition(Notification.Position.BOTTOM_END);
    }

    private void loadPosts() {
        posts = service.listNext50(page);

        if (posts.isEmpty()) {
            postContainer.add(new Span("No posts found"));
            return;
        }

        currentIndex = 0;
        showPost();
    }

    private void showPost() {
        postContainer.removeAll();

        Post post = posts.get(currentIndex);

        VerticalLayout postLayout = new VerticalLayout();
        postLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Image image = new Image(
                post.getImagePath() != null ? post.getImagePath() : "images/burro2.jpg",
                "Post image");

        image.setWidth("250px");

        Span description = new Span(post.getDescription());
        description.getStyle()
                .set("text-align", "center")
                .set("max-width", "300px");

        postLayout.add(image, description);

        postContainer.add(postLayout);
    }
}