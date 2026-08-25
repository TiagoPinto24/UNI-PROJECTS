package com.example.sponsordonkey.services.EmailSender;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sponsordonkey.data.User.User;
import com.example.sponsordonkey.data.User.UserRepository;
import com.example.sponsordonkey.data.User.UserTypes;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    public List<EmailDTO> getEmailsToSend() {

        List<User> users = userRepository.findAll();

        List<EmailDTO> emails = new ArrayList<>();

        for (User u : users) {

            if (u.getUserType() == UserTypes.SPONSOR) {
                EmailDTO dto = new EmailDTO();
                dto.setEmail(u.getEmail());
                dto.setSubject("Sponsor update");
                dto.setBody("Hello sponsor, you have new updates.\n\nThis is the most recent activity of your sponsored donkey:\n\n(Information)\n\nThank you so much for your support");

                emails.add(dto);
            }
        }

        return emails;
    }
}