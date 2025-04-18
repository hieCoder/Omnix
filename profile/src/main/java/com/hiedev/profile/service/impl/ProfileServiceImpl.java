package com.hiedev.profile.service.impl;

import com.hiedev.event.dto.ProfileEventDTO;
import com.hiedev.profile.entity.Profile;
import com.hiedev.profile.mapper.ProfileMapper;
import com.hiedev.profile.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl  {

    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    @KafkaListener(topics = "user-registration", groupId = "profile-group")
    public void addProfile(ProfileEventDTO profileEventDTO) {
        Profile profile = profileMapper.toProfile(profileEventDTO);
        profileRepository.save(profile);
    }

}
