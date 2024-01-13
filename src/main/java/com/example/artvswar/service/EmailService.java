package com.example.artvswar.service;

import com.example.artvswar.model.Account;
import com.example.artvswar.model.Order;

public interface EmailService {

    void sendImageRejectionMail(String publicId, String moderationResponse);

    String sendTestEmail(String email);

    void contactUsEmail(String email, String message);

    void purchasePaintingToCustomerEmail(Order order, Account account, String titleWithAuthor);
}
