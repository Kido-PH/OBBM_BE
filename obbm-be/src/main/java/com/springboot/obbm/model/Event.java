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
@Entity(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eventId")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer eventId;

    @Column(name = "event_name")
    String name;

    @Column(name = "event_totalcost")
    Double totalcost;

    @Column(name = "event_description")
    String description;

    @Column(name = "event_image")
    String image;

    @Column(name = "event_ismanaged")
    Boolean ismanaged;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User users;

    @OneToMany(mappedBy = "events")
    List<Menu> listMenu;

    @OneToMany(mappedBy = "events")
    List<EventServices> listEventServices;

    @OneToMany(mappedBy = "events")
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
