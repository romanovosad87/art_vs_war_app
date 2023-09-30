package com.example.artvswar.util;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminUserGlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.DeleteUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AwsCognitoClient {
    private static final String REFRESH_TOKEN_NAME = "REFRESH_TOKEN";
    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Value("${cognito.userpool.id}")
    private String userPoolId;

    @Value("${cognito.client.id}")
    private String clientId;
    public void addUserToGroup(String username, String groupname){
        AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                .withGroupName(groupname)
                .withUserPoolId(userPoolId)
                .withUsername(username);

        cognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);
    }

    public AuthenticationResultType getNewTokens(String refreshToken) {
        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
        authRequest.withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .withClientId(clientId)
                .withUserPoolId(userPoolId)
                .withAuthParameters(Map.of(REFRESH_TOKEN_NAME, refreshToken));

        AdminInitiateAuthResult adminInitiateAuthResult
                = cognitoIdentityProvider.adminInitiateAuth(authRequest);

        return adminInitiateAuthResult.getAuthenticationResult();
    }

    public void deleteUser(Jwt jwt) {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
        deleteUserRequest.setAccessToken(jwt.getTokenValue());

        System.out.println(cognitoIdentityProvider.deleteUser(deleteUserRequest).toString());
    }

    public void signOutUser(String username) {
        AdminUserGlobalSignOutRequest signOutRequest = new AdminUserGlobalSignOutRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username);

        System.out.println(cognitoIdentityProvider.adminUserGlobalSignOut(signOutRequest).toString());
    }
}
