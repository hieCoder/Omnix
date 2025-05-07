package com.hiedev.notification.service;

import com.hiedev.event.EmailRequest;
import com.hiedev.notification.dto.request.SendEmailRequest;
import com.hiedev.notification.dto.request.SenderRequest;
import com.hiedev.notification.repository.httpclient.EmailApiClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmailService {

    EmailApiClient emailApiClient;

    @NonFinal
    @Value("${brevo.key}")
    String apiKey;

    @NonFinal
    @Value("${brevo.senderEmail}")
    String senderEmail;

    @NonFinal
    @Value("${brevo.senderName}")
    String senderName;

    @KafkaListener(topics = "user-registration", groupId = "notification-group")
    public void sendEmail(EmailRequest emailRequest) {
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .sender(SenderRequest.builder().email(senderEmail).name(senderName).build())
                .to(List.of(emailRequest.getTo()))
                .subject(emailRequest.getSubject())
                .htmlContent("<html><body>" + emailRequest.getHtmlContent() + "</body></html>")
                .build();
        emailApiClient.sendEmail(apiKey, sendEmailRequest);
    }

}
