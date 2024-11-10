package com.springboot.obbm.dto.request;

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
public class ExchangeTokenRequest {
    String code;
    String clientId;
    String clientSecret;
    String redirectUri;
    String grantType;
}
