package com.hiedev.notification.repository.httpclient;

import com.hiedev.notification.dto.request.SendEmailRequest;
import com.hiedev.notification.dto.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email-service", url = "https://api.brevo.com")
public interface EmailApiClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody SendEmailRequest body);
}
