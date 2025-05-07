package com.hiedev.identity.infrastructure.client.openfeign;

import com.hiedev.identity.application.dto.request.ProfileRequest;
import com.hiedev.identity.application.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "profile-service")
public interface ProfileClient {
    @PostMapping(value = "/profile/createProfile", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> createProfile(ProfileRequest profileRequest);

    @GetMapping(value = "/profile/getProfile/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId);
}
