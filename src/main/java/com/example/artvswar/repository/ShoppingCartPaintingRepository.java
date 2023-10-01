package com.example.artvswar.repository;

import com.example.artvswar.model.ShoppingCartPainting;
import com.example.artvswar.model.ShoppingCartPaintingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ShoppingCartPaintingRepository extends JpaRepository<ShoppingCartPainting, ShoppingCartPaintingKey> {
    @Modifying
    @Query("delete from ShoppingCartPainting scp where scp.paintingId =?1 and scp.shoppingCartId = ?2")
    void deleteCustom (Long painingId, Long shoppingCartId);

    @Query("select scp.paintingId from ShoppingCartPainting scp join scp.shoppingCart sc join sc.account ac where ac.cognitoSubject= ?1")
    List<Long> getListOfPaintingIds(String cognitoSubject);
}
