package com.hiedev.identity.application.service.impl;

import com.hiedev.identity.application.dto.request.GoogleTokenRequest;
import com.hiedev.identity.application.dto.response.GoogleTokenResponse;
import com.hiedev.identity.application.dto.response.GoogleUserInfoResponse;
import com.hiedev.identity.application.service.GoogleService;
import com.hiedev.identity.infrastructure.client.openfeign.GoogleApiClient;
import com.hiedev.identity.infrastructure.client.openfeign.GoogleGetUserInfoClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    GoogleApiClient googleApiClient;
    GoogleGetUserInfoClient getUserInfo;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String clientId;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String redirectUri;

    @Override
    public GoogleUserInfoResponse getUserInfo(String code) {
        String grantType = "authorization_code";
        GoogleTokenRequest googleTokenRequest = GoogleTokenRequest.builder()
                .code(code)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(grantType)
                .build();

        GoogleTokenResponse googleTokenResponse = googleApiClient.getToken(googleTokenRequest);
        return getUserInfo.getUserInfo("json", googleTokenResponse.getAccessToken());
    }

}
