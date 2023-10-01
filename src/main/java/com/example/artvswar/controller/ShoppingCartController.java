package com.example.artvswar.controller;

import com.example.artvswar.dto.response.shoppingCart.ShoppingCartResponseDto;
import com.example.artvswar.service.ShoppingCartPaintingService;
import com.example.artvswar.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private static final String SUBJECT = "sub";
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartPaintingService shoppingCartPaintingService;

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDto> get(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        ShoppingCartResponseDto dto = shoppingCartService.getShoppingCart(subject);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/{paintingId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addToCart(@PathVariable Long paintingId,
                          @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        shoppingCartPaintingService.save(paintingId, subject);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addSeveralItemsToCart(@RequestParam Set<Long> paintingIds,
                                      @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        shoppingCartPaintingService.saveAll(paintingIds, subject);
    }

    @DeleteMapping("/{paintingId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFromCart(@PathVariable Long paintingId,
                          @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        shoppingCartPaintingService.remove(paintingId, subject);
    }
}
