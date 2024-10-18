package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contractId;

    @Column(name = "contract_name")
    private String name;

    @Column(name = "contract_type")
    private String type;

    @Column(name = "contract_totalcost")
    private double totalcost;

    @Column(name = "contract_status")
    private boolean status;

    @Column(name = "contract_paymentstatus")
    private String paymentstatus;

    @Column(name = "contract_organizdate")
    private LocalDateTime organizdate;

    @Column(name = "contract_description")
    private String description;

    @Column(name = "contract_custname")
    private String custname;

    @Column(name = "contract_custphone")
    private String custphone;

    @Column(name = "contract_custmail")
    private String custmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locations;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event events;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menus;

    @OneToMany(mappedBy = "contracts")
    private List<StockRequest> listStockrequests;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
