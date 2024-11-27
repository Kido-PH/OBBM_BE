package com.springboot.obbm.dto.user.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateStaffRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    @NonNull
    String username;
    String password;
    String email;
    List<String> roles;
}
