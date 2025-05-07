package com.hiedev.event;

import com.hiedev.notification.dto.request.RecipientRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    RecipientRequest to;
    String subject;
    String htmlContent;
}
