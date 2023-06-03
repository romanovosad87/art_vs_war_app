package com.example.artvswar.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AwsCognitoRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String COGNITO_GROUP_CLAIM = "cognito:groups";
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        List<String> cognitoGroup  = source.getClaimAsStringList(COGNITO_GROUP_CLAIM);
        if (cognitoGroup == null || cognitoGroup.isEmpty()) {
            return new ArrayList<>();
        }
        return cognitoGroup.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
