package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "eventservice")
public class EventService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventServiceId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event events;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service services;

    @Column(name = "eventservice_cost")
    private double eventServiceCost;

    @Column(name = "eventservice_quantity")
    private int eventServiceQuantity;
}
