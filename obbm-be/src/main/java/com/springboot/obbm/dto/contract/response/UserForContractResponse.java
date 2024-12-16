package com.springboot.obbm.dto.contract.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserForContractResponse {
    String userId;
    String username;
    String fullname;
    Boolean gender;
    String residence;
    LocalDate dob;
    String email;
    String phone;
    String image;
    String citizenIdentity;
    Boolean isStatus = false;
    Boolean noPassword = false;
}