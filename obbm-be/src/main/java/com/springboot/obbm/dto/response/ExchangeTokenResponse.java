package com.springboot.obbm.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) //Viết tất cả các thuộc tính theo "snake_case"
public class ExchangeTokenResponse {
    String accessToken;
    Long expiresIn;
    String refreshToken;
    String scope;
    String tokenType;
    String idToken;
}
