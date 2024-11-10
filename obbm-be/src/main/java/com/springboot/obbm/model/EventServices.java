package com.springboot.obbm.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity(name = "eventservice")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class EventServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer eventserviceId;


    @Column(name = "eventservice_quantity")
    Integer quantity;

    @Column(name = "eventservice_cost")
    Double cost;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event events;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Services services;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User users;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
