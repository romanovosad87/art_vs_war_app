package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.authorshippingaddress.AuthorShippingAddressRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingAddressResponseDto;
import com.example.artvswar.model.AuthorShippingAddress;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Component;

@Component
public class AuthorShippingAddressMapper {

    public AuthorShippingAddress toModel(AuthorShippingAddressRequestDto dto) {
        AuthorShippingAddress authorShippingAddress = new AuthorShippingAddress();
        authorShippingAddress.setAddressLine1(dto.getAddressLine1());
        authorShippingAddress.setAddressLine2(dto.getAddressLine2());
        authorShippingAddress.setCity(dto.getCity());
        authorShippingAddress.setState(dto.getState());
        authorShippingAddress.setCountry(dto.getCountry());
        authorShippingAddress.setCountryCode(CountryCode.findByName(dto.getCountry()).get(0).name());
        authorShippingAddress.setPostalCode(dto.getPostalCode());
        authorShippingAddress.setPhone(dto.getPhone());
        return authorShippingAddress;
    }

    public AuthorShippingAddress toModelUpdate(AuthorShippingAddressRequestDto dto,
                                               AuthorShippingAddress authorShippingAddress) {
        authorShippingAddress.setAddressLine1(dto.getAddressLine1());
        authorShippingAddress.setAddressLine2(dto.getAddressLine2());
        authorShippingAddress.setCity(dto.getCity());
        authorShippingAddress.setState(dto.getState());
        authorShippingAddress.setCountry(dto.getCountry());
        authorShippingAddress.setCountryCode(CountryCode.findByName(dto.getCountry()).get(0).name());
        authorShippingAddress.setPostalCode(dto.getPostalCode());
        authorShippingAddress.setPhone(dto.getPhone());
        return authorShippingAddress;
    }

    public ShippingAddressResponseDto toDto(AuthorShippingAddress authorShippingAddress) {
        return new ShippingAddressResponseDto(
                authorShippingAddress.getAddressLine1(),
                authorShippingAddress.getAddressLine2(),
                authorShippingAddress.getCity(),
                authorShippingAddress.getState(),
                authorShippingAddress.getCountry(),
                authorShippingAddress.getCountryCode(),
                authorShippingAddress.getPostalCode(),
                authorShippingAddress.getPhone()
        );
    }
}
