package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "eventservice")
public class EventService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventserviceId;


    @Column(name = "eventservice_quantity")
    private int quantity;

    @Column(name = "eventservice_cost")
    private double cost;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event events;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service services;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
