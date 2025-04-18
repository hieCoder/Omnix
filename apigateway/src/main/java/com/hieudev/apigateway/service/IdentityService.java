package com.hieudev.apigateway.service;

import com.hieudev.apigateway.dto.request.IntrospectRequest;
import com.hieudev.apigateway.dto.response.ApiResponse;
import com.hieudev.apigateway.dto.response.IntrospectResponse;
import com.hieudev.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {

    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }

}
