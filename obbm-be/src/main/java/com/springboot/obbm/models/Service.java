package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "service_isavailability")
    private boolean serviceIsAvailability;

    @Column(name = "service_price")
    private double servicePrice;

    @Column(name = "isdeleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "services")
    private Set<EventService> listEventService;
}
