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
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setTimePeriod(30); // Thời gian bước là 30 giây
        verifier.setAllowedTimePeriodDiscrepancy(1); // Cho phép sai lệch 1 bước thời gian (±30 giây)
        return verifier.isValidCode(secretKey, totp);
    }

}
