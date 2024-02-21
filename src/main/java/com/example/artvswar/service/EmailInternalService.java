package com.example.artvswar.service;

public interface EmailInternalService extends EmailService {
    void sendImageRejectionMail(String publicId, String moderationResponse);

    void contactUsEmail(String email, String message);

}
