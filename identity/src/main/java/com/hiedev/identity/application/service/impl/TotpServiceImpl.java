package com.hiedev.identity.application.service.impl;

import com.hiedev.identity.presentation.exception.TOTPException;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

@Service
public class TotpServiceImpl {

    public String generateSecretKey() {
        DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
        return secretGenerator.generate();
    }

    public String generateTOTP(String secretKey) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);

        long currentTime = timeProvider.getTime() / 30;
        try {
            return codeGenerator.generate(secretKey, currentTime);
        } catch (CodeGenerationException e) {
            throw new TOTPException("Error generating TOTP code", e);
        }
    }

    public boolean validateTOTP(String secretKey, String totp) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider); // 60 giây, sai lệch 1 step
        return verifier.isValidCode(secretKey, totp);
    }

}
