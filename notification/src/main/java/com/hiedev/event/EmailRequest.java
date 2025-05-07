package com.hiedev.event;

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
