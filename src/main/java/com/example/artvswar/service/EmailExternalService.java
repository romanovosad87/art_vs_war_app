package com.example.artvswar.service;

import com.example.artvswar.model.Account;
import com.example.artvswar.model.AccountEmailData;
import com.example.artvswar.model.Order;
import com.example.artvswar.model.Painting;
import com.stripe.model.ShippingDetails;

public interface EmailExternalService extends EmailService {
    void purchasePaintingToCustomerEmail(Order order, ShippingDetails shippingDetails, Account account, String titleWithAuthor);

    void purchasePaintingToAuthorEmail(ShippingDetails shippingDetails, AccountEmailData accountEmailData,
                                       String authorFullName, Painting painting);
}
