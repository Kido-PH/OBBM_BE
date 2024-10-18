package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(name = "service_name")
    private String name;

    @Column(name = "service_type")
    private String type;

    @Column(name = "service_price")
    private double price;

    @Column(name = "service_description")
    private String description;

    @Column(name = "service_status")
    private boolean status;

    @OneToMany(mappedBy = "services")
    private List<EventService> listEventService;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
