package com.example.artvswar.repository.shoppingcart;

import com.example.artvswar.dto.response.shoppingcart.ShoppingCartResponseDto;

public interface CustomShoppingCartRepository {

    ShoppingCartResponseDto get(String cognitoSubject);

}
