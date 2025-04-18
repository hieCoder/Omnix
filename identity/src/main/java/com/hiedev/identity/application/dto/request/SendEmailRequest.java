package com.hiedev.identity.application.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SendEmailRequest {
    SenderRequest sender;
    List<RecipientRequest> to;
    String subject;
    String htmlContent;
}
