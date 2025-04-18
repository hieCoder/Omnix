package com.hiedev.identity.infrastructure.client.openfeign;

import com.hiedev.identity.application.dto.response.GoogleUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-user-info", url = "https://www.googleapis.com")
public interface GoogleGetUserInfoClient {
    @GetMapping("/oauth2/v1/userinfo")
    GoogleUserInfoResponse getUserInfo(@RequestParam("alt") String alt,
                                       @RequestParam("access_token") String accessToken);
}
