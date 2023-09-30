package com.example.artvswar.repository.shoppingCart;

import com.example.artvswar.dto.response.shoppingCart.ShoppingCartResponseDto;

public interface CustomShoppingCartRepository {

    ShoppingCartResponseDto get(String cognitoSubject);

}
