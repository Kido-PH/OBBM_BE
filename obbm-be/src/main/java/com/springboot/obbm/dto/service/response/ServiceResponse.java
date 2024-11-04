package com.springboot.obbm.dto.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.obbm.models.EventService;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {
    Integer serviceId;
    String name;
    String type;
    Double price;
    String description;
    Boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}