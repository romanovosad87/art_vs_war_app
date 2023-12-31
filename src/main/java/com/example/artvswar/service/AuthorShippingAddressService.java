package com.example.artvswar.service;

import com.example.artvswar.dto.request.authorshippingaddress.AuthorShippingAddressRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingAddressResponseDto;

public interface AuthorShippingAddressService {
    ShippingAddressResponseDto create(AuthorShippingAddressRequestDto dto, String cognitoSubject);
    ShippingAddressResponseDto update(AuthorShippingAddressRequestDto dto, String cognitoSubject);
    ShippingAddressResponseDto findByAuthor(String cognitoSubject);

    void delete(String cognitoSubject);
}
