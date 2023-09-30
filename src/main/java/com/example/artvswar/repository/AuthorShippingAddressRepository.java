package com.example.artvswar.repository;

import com.example.artvswar.model.AuthorShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorShippingAddressRepository extends JpaRepository<AuthorShippingAddress, String> {
    <T> Optional<T> findByAuthorCognitoSubject(Class<T> type, String cognitoSubject);
}
