package com.hiedev.identity.infrastructure.client.openfeign;

import com.hiedev.identity.application.dto.request.GoogleTokenRequest;
import com.hiedev.identity.application.dto.response.GoogleTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "google", url = "https://oauth2.googleapis.com")
public interface GoogleApiClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse getToken(@QueryMap GoogleTokenRequest googleTokenRequest);
}
