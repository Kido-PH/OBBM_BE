package com.springboot.obbm.dto.services.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicesResponse {
    Integer serviceId;
    String name;
    String type;
    Double price;
    String description;
    Boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}