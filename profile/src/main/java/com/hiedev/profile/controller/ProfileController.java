package com.hiedev.profile.controller;

import com.hiedev.profile.dto.request.ProfileRequest;
import com.hiedev.profile.dto.response.ProfileResponse;
import com.hiedev.profile.entity.Profile;
import com.hiedev.profile.service.impl.ProfileServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProfileController {

    ProfileServiceImpl profileService;

    @PostMapping("/createProfile")
    public ResponseEntity<Boolean> createProfile(@RequestBody ProfileRequest profileRequest) {
        // Logic to create a profile
        return ResponseEntity.ok(profileService.createProfile(profileRequest));
    }

    @GetMapping("/getProfile/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

}
