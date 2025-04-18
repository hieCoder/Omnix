package com.hiedev.identity.application.service;

import com.hiedev.identity.application.dto.response.GoogleUserInfoResponse;

public interface GoogleService {
    GoogleUserInfoResponse getUserInfo(String code);
}
