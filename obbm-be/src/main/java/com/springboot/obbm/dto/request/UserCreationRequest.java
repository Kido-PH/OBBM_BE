package com.springboot.obbm.dto.request;

import com.springboot.obbm.validator.dob.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_INVALID")
    @NonNull
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String fullname;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}
