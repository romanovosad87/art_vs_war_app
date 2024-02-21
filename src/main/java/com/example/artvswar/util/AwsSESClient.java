package com.example.artvswar.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesRequest;
import software.amazon.awssdk.services.ses.model.GetIdentityVerificationAttributesResponse;
import software.amazon.awssdk.services.ses.model.IdentityVerificationAttributes;
import software.amazon.awssdk.services.ses.model.VerificationStatus;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.CreateEmailIdentityRequest;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AwsSESClient {

    private final SesV2Client sesV2Client;

    private final SesClient sesClient;

    public void createEmailIdentity(String email) {
        CreateEmailIdentityRequest request = CreateEmailIdentityRequest
                .builder()
                .emailIdentity(email)
                .build();
        sesV2Client.createEmailIdentity(request);
    }

    public String getIdentityVerification(String identity) {
        GetIdentityVerificationAttributesRequest request
                = GetIdentityVerificationAttributesRequest
                .builder()
                .identities(identity)
                .build();

        GetIdentityVerificationAttributesResponse attributes = sesClient.getIdentityVerificationAttributes(request);
        Map<String, IdentityVerificationAttributes> verificationAttributes = attributes.verificationAttributes();
        IdentityVerificationAttributes identityVerificationAttributes = verificationAttributes.get(identity);
        VerificationStatus verificationStatus = identityVerificationAttributes.verificationStatus();
        return verificationStatus.toString();
    }
}
