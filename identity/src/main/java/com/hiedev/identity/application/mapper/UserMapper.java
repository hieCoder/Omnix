package com.hiedev.identity.application.mapper;

import com.hiedev.event.dto.ProfileEventDTO;
import com.hiedev.identity.application.dto.request.CreateUserRequest;
import com.hiedev.identity.application.dto.response.GoogleUserInfoResponse;
import com.hiedev.identity.application.dto.response.UserResponse;
import com.hiedev.identity.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(CreateUserRequest createUserRequest);

    UserResponse toUserResponseDTO(User user);

    @Mapping(target = "userId", ignore = true)
    ProfileEventDTO toUserEventResponseDTO(CreateUserRequest CreateUserRequest);

    User toUser(GoogleUserInfoResponse googleUserInfoResponse);
}
