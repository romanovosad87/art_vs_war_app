package com.example.artvswar.repository.shoppingCart;

import com.example.artvswar.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        CustomShoppingCartRepository {
    <T> Optional<T> findByAccount_CognitoSubject(Class<T> type, String cognitoSubject);

    Optional<ShoppingCart> findByAccount_StripeCustomerId(String stripeCustomId);
}
