package com.hiedev.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileEventDTO {
    Long userId;
    String firstName;
    String lastName;
    LocalDate dob;
}
