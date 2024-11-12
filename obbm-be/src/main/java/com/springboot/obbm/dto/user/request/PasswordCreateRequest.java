package com.springboot.obbm.dto.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.dto.user.response.RoleForUserResponse;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordCreateRequest {
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
}
