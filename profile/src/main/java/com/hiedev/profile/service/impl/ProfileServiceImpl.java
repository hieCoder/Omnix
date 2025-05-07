package com.hiedev.profile.service.impl;

import com.hiedev.profile.dto.request.ProfileRequest;
import com.hiedev.profile.dto.response.ProfileResponse;
import com.hiedev.profile.entity.Profile;
import com.hiedev.profile.exception.NotFoundException;
import com.hiedev.profile.mapper.ProfileMapper;
import com.hiedev.profile.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl  {

    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public boolean createProfile(ProfileRequest profileRequest) {
        try {
            Profile profile = profileMapper.toProfile(profileRequest);
            profileRepository.save(profile);

            return true;
        } catch (Exception e) {
            log.error("Error creating profile: {}", e.getMessage());
            return false;
        }
    }

    public ProfileResponse getProfile(Long userId) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("Profile not found for userId: {}", userId);
            return new NotFoundException("Profile not found");
        });

        return profileMapper.toProfileResponse(profile);
    }

}
