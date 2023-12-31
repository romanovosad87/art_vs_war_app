package com.example.artvswar.service;

import com.example.artvswar.dto.response.shoppingcart.ShoppingCartResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCart(String cognitoSubject);
    ShoppingCart getReference(Long id);

    ShoppingCart get(String accountCognitoSubject);

    ShoppingCart create(Account account);
}
