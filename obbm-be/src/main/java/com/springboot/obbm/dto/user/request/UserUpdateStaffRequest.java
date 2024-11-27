package com.springboot.obbm.dto.user.request;

import com.springboot.obbm.validator.dob.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateStaffRequest {
    String password;
    String fullname;
    String residence;
    Boolean gender;
    String email;
    String phone;
    String image;
    String citizenIdentity;
    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
    List<String> roles;
}
