package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "location_capacity")
    private int locationCapacity;

    @Column(name = "location_table")
    private int locationTable;

    @Column(name = "location_rentcost")
    private double locationRentCost;

    @Column(name = "location_description")
    private String locationDescription;

    @Column(name = "isdeleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "locations")
    private Set<Contract> listContract;
}
