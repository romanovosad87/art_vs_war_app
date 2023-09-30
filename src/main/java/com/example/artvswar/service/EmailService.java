package com.example.artvswar.service;

public interface EmailService {

    void sendImageRejectionMail(String publicId, String moderationResponse);

    String sendTestEmail(String email);

    void contactUsEmail(String email, String message);
}
