package com.example.artvswar.service;

import java.util.Set;

public interface ShoppingCartPaintingService {
    void save(Long paintingId, String accountCognitoSubject);
    void remove(Long paintingId, String accountCognitoSubject);
    void saveAll(Set<Long> paintingsIds, String accountCognitoSubject);

}
