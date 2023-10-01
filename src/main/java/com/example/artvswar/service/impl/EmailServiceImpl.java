package com.example.artvswar.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailResult;
import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.service.AdminService;
import com.example.artvswar.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final MailSender mailSender;
    private final AdminService adminService;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Override
    public void sendImageRejectionMail(String publicId, String moderationResponse) {
        String adminEmails = adminService.getAll()
                .stream()
                .map(AdminResponseDto::getEmail)
                .collect(Collectors.joining(", "));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Art vs War (moderation) <moderation@artvswar.gallery>");
        simpleMailMessage.setTo(adminEmails);
        simpleMailMessage.setSubject("AWS_REK moderation rejection");
        simpleMailMessage.setText(String.format("Image with public_id - %s was rejected by AWS-Rekognition."
                + System.lineSeparator()
                + "Moderation response:  %s." + System.lineSeparator()
                + "Please make manual moderation in Cloudinary dashboard.", publicId, moderationResponse));
        mailSender.send(simpleMailMessage);
    }

    @Override
    public String sendTestEmail(String email) {
        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();

        sendTemplatedEmailRequest.setTemplate("Test_Email-1695674011980");
        sendTemplatedEmailRequest.setSource("Art vs War <info@artvswar.gallery>");
        sendTemplatedEmailRequest.setDestination(new Destination(List.of(email)));
        sendTemplatedEmailRequest.setTemplateData("{\"subject\":\"First Test Email\"}");

        SendTemplatedEmailResult sendTemplatedEmailResult = amazonSimpleEmailService
                .sendTemplatedEmail(sendTemplatedEmailRequest);
        return sendTemplatedEmailResult.getMessageId();
    }

    @Override
    public void contactUsEmail(String email, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("contact_us@artvswar.gallery");
        simpleMailMessage.setTo("info@artvswar.gallery");
        simpleMailMessage.setSubject("Email from: " + email);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }
}
