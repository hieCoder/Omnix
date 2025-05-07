package com.hiedev.notification.dto.request;

import com.hiedev.event.RecipientRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    SenderRequest sender;
    List<RecipientRequest> to;
    String subject;
    String htmlContent;
}
