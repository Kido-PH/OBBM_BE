package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(name = "event_name")
    private String name;

    @Column(name = "event_totalcost")
    private double totalcost;

    @Column(name = "event_description")
    private String description;

    @OneToMany(mappedBy = "events")
    private List<Menu> listMenu;

    @OneToMany(mappedBy = "events")
    private List<EventService> listEventService;

    @OneToMany(mappedBy = "events")
    private List<Contract> listContract;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
