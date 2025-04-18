package com.hiedev.identity.infrastructure.security;

import com.hiedev.identity.domain.enums.PermissionEnum;
import com.hiedev.identity.domain.enums.RoleEnum;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expirationMs}")
    long expiration;

    @Value("${jwt.refreshExpirationMs}")
    long refreshExpiration;

    public String generateToken(String email, Set<RoleEnum> roles, Set<PermissionEnum> permissions, long expiration) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("book-media")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expiration))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        JWSSigner signer = new MACSigner(secret);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String generateAccessToken(String email, Set<RoleEnum> roles, Set<PermissionEnum> permissions) throws JOSEException {
        return generateToken(email, roles, permissions, expiration);
    }

    public String generateRefreshToken(String email, Set<RoleEnum> roles, Set<PermissionEnum> permissions) throws JOSEException {
        return generateToken(email, roles, permissions, refreshExpiration);
    }

}
