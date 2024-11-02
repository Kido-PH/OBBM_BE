package com.springboot.obbm.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "service")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer serviceId;

    @Column(name = "service_name")
    String name;

    @Column(name = "service_type")
    String type;

    @Column(name = "service_price")
    Double price;

    @Column(name = "service_description")
    String description;

    @Column(name = "service_status")
    Boolean status;

    @OneToMany(mappedBy = "services")
    List<EventService> listEventService;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
