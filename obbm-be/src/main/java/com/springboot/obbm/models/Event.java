package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_totalcost")
    private double eventTotalCost;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "isdeleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "events")
    private Set<EventService> listEventService;

    @OneToMany(mappedBy = "events")
    private Set<Contract> listContract;
}
