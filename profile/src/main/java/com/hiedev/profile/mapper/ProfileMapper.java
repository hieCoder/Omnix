package com.hiedev.profile.mapper;

import com.hiedev.event.dto.ProfileEventDTO;
import com.hiedev.profile.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileEventDTO profileEventDTO);
}
