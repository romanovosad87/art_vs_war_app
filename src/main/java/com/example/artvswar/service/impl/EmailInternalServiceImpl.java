package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.service.AdminService;
import com.example.artvswar.service.EmailInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailInternalServiceImpl implements EmailInternalService {
    public static final String CLOUDINARY_LINK = "https://console.cloudinary.com/console"
            + "/c-1e98a800837a96a82947e8ca3b3fd0/media_library/search?q=&view_mode=grid";
    public static final String MANUAL_MODERATION_LINK = "https://artvswar.gallery/images-validation";
    public static final String BASIC_EMAIL = "info@artvswar.gallery";
    private final MailSender mailSender;
    private final AdminService adminService;
    @Override
    public void sendImageRejectionMail(String publicId, String moderationResponse) {
        String[] adminEmails = getAdminEmails();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Art vs War (moderation) <moderation@artvswar.gallery>");
        simpleMailMessage.setTo(adminEmails);
        simpleMailMessage.setSubject("AWS_REK moderation rejection");
        simpleMailMessage.setText(getMessage(publicId, moderationResponse));
        mailSender.send(simpleMailMessage);
    }

    @Override
    public void contactUsEmail(String email, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("contact_us@artvswar.gallery");
        simpleMailMessage.setTo(BASIC_EMAIL);
        simpleMailMessage.setSubject("Email from: " + email);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }

    private String[] getAdminEmails() {
        return adminService.getAll()
                .stream()
                .map(AdminResponseDto::getEmail)
                .toArray(String[]::new);
    }

    private String getMessage(String publicId, String moderationResponse) {
        return String.format("Image was rejected by AWS-Rekognition."
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "public_id:  %s"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "Moderation response:  %s."
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "The link to manual moderation:"
                        + System.lineSeparator()
                        + "%s"
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "The link to Cloudinary dashboard:"
                        + System.lineSeparator()
                        + "%s",
                publicId, moderationResponse, MANUAL_MODERATION_LINK, CLOUDINARY_LINK);
    }
}
