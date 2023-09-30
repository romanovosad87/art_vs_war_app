package com.example.artvswar.service.impl;

import com.example.artvswar.model.ShoppingCartPainting;
import com.example.artvswar.repository.ShoppingCartPaintingRepository;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.ShoppingCartPaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShoppingCartPaintingServiceImpl implements ShoppingCartPaintingService {

    private final ShoppingCartPaintingRepository shoppingCartPaintingRepository;
    private final AccountService accountService;

    @Override
    @Transactional
    public void save(Long paintingId, String accountCognitoSubject) {
        ShoppingCartPainting shoppingCartPainting = new ShoppingCartPainting();
        shoppingCartPainting.setPaintingId(paintingId);
        Long shoppingCartId = accountService.getIdByCognitoSubject(accountCognitoSubject);
        shoppingCartPainting.setShoppingCartId(shoppingCartId);
        shoppingCartPaintingRepository.save(shoppingCartPainting);
    }

    @Override
    @Transactional
    public void remove(Long paintingId, String accountCognitoSubject) {
        Long shoppingCartId = accountService.getIdByCognitoSubject(accountCognitoSubject);
        shoppingCartPaintingRepository.deleteCustom(paintingId, shoppingCartId);
    }

    @Override
    @Transactional
    public void saveAll(Set<Long> paintingsIds, String accountCognitoSubject) {

        List<Long> paintingIdsInShoppingCart = shoppingCartPaintingRepository
                .getListOfPaintingIds(accountCognitoSubject);

        List<Long> newPaintingIds = paintingsIds.stream()
                .filter(id -> !paintingIdsInShoppingCart.contains(id))
                .collect(Collectors.toList());

        if (newPaintingIds.size() != 0) {
            List<ShoppingCartPainting> shoppingCartPaintingList = new ArrayList<>();
            for (Long paintingId : newPaintingIds) {
                ShoppingCartPainting shoppingCartPainting = new ShoppingCartPainting();
                shoppingCartPainting.setPaintingId(paintingId);
                Long shoppingCartId = accountService.getIdByCognitoSubject(accountCognitoSubject);
                shoppingCartPainting.setShoppingCartId(shoppingCartId);
                shoppingCartPaintingList.add(shoppingCartPainting);
            }
            shoppingCartPaintingRepository.saveAll(shoppingCartPaintingList);
        }
    }
}
