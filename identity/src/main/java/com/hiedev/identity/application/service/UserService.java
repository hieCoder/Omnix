package com.hiedev.identity.application.service;

import com.hiedev.identity.application.dto.response.PaginatedResponse;
import com.hiedev.identity.application.dto.response.UserResponse;

public interface UserService {
    PaginatedResponse<UserResponse> findAllUsers(int page, int site, String sortBy, String sortDir);
    UserResponse getUserDetail();
}
