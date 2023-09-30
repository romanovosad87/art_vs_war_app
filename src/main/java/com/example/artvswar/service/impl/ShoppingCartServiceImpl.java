package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.shoppingCart.ShoppingCartResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.ShoppingCart;
import com.example.artvswar.repository.shoppingCart.ShoppingCartRepository;
import com.example.artvswar.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart(String cognitoSubject) {
        return shoppingCartRepository.get(cognitoSubject);

    }

    @Override
    public ShoppingCart getReference(Long id) {
        return shoppingCartRepository.getReferenceById(id);
    }

    @Override
    public ShoppingCart get(String accountCognitoSubject) {
        return shoppingCartRepository.findByAccount_CognitoSubject(ShoppingCart.class,
                accountCognitoSubject).orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find shopping cart by accountCognitoSubject: %s",
                                accountCognitoSubject)));
    }

    @Override
    @Transactional
    public ShoppingCart create(Account account) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setAccount(account);
        return shoppingCartRepository.save(shoppingCart);
    }
}
