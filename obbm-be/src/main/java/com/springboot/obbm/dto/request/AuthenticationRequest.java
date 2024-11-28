package com.springboot.obbm.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @NotBlank(message = "FIELD_NOT_BLANK")
    private String username;

    @NotBlank(message = "FIELD_NOT_BLANK")
    private String password;
}