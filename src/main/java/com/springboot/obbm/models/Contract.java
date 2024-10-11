package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locations;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event events;

    @Column(name = "contract_datecreate")
    private Date contractDateCreate;

    @Column(name = "contract_status")
    private boolean contractStatus;

    @Column(name = "contract_totalcost")
    private double contractTotalCost;

    @Column(name = "contract_details")
    private String contractDetails;
}
