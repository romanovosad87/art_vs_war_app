package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.AuthorShippingAddressMapper;
import com.example.artvswar.dto.request.authorshippingaddress.AuthorShippingAddressRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingAddressResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorShippingAddress;
import com.example.artvswar.repository.AuthorShippingAddressRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.AuthorShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorShippingAddressServiceImpl implements AuthorShippingAddressService {
    private final AuthorShippingAddressRepository authorShippingAddressRepository;
    private final AuthorService authorService;
    private final AuthorShippingAddressMapper authorShippingAddressMapper;
    @Override
    @Transactional
    public ShippingAddressResponseDto create(AuthorShippingAddressRequestDto dto, String cognitoSubject) {
        AuthorShippingAddress authorShippingAddress = authorShippingAddressMapper.toModel(dto);
        Author author = authorService.getAuthorByCognitoSubject(cognitoSubject);
        authorShippingAddress.setAuthor(author);
        AuthorShippingAddress savedAddress = authorShippingAddressRepository.save(authorShippingAddress);
        return authorShippingAddressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional
    public ShippingAddressResponseDto update(AuthorShippingAddressRequestDto dto, String cognitoSubject) {
        AuthorShippingAddress authorShippingAddress = authorShippingAddressRepository.findByAuthorCognitoSubject(AuthorShippingAddress.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find shipment address by author cognito subject: %s", cognitoSubject)));
        AuthorShippingAddress updateAuthorShippingAddress = authorShippingAddressMapper.toModelUpdate(dto, authorShippingAddress);
        return authorShippingAddressMapper.toDto(updateAuthorShippingAddress);
    }

    @Override
    public ShippingAddressResponseDto findByAuthor(String cognitoSubject) {
        return authorShippingAddressRepository.findByAuthorCognitoSubject(ShippingAddressResponseDto.class, cognitoSubject)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find shipment address by author cognito subject: %s", cognitoSubject)));
    }

    @Override
    @Transactional
    public void delete(String cognitoSubject) {
        authorShippingAddressRepository.deleteById(cognitoSubject);
    }
}
