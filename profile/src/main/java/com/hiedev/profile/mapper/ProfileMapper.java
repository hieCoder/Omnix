package com.hiedev.profile.mapper;

import com.hiedev.profile.dto.request.ProfileRequest;
import com.hiedev.profile.dto.response.ProfileResponse;
import com.hiedev.profile.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileRequest profileRequest);
    ProfileResponse toProfileResponse(Profile profile);
}
