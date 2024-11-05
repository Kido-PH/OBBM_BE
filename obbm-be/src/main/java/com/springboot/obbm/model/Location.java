package com.springboot.obbm.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "location")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer locationId;

    @Column(name = "location_name")
    String name;

    @Column(name = "location_type")
    String type;

    @Column(name = "location_address")
    String address;

    @Column(name = "location_capacity")
    Integer capacity;

    @Column(name = "location_table")
    Integer table;

    @Column(name = "location_cost")
    Double cost;

    @Column(name = "location_description")
    String description;

    @Column(name = "location_iscustom")
    Boolean isCustom;

    @Column(name = "location_status")
    String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User users;

    @OneToMany(mappedBy = "locations")
    List<Contract> listContract;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
