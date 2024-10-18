package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    @Column(name = "location_name")
    private String name;

    @Column(name = "location_type")
    private String type;

    @Column(name = "location_address")
    private String address;

    @Column(name = "location_capacity")
    private int capacity;

    @Column(name = "location_table")
    private int table;

    @Column(name = "location_cost")
    private double cost;

    @Column(name = "location_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @OneToMany(mappedBy = "locations")
    private List<Contract> listContract;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
