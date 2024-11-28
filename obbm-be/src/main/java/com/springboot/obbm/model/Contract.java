package com.springboot.obbm.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "contract")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer contractId;

    @Column(name = "contract_name")
    String name;

    @Column(name = "contract_type")
    String type;

    @Column(name = "contract_guest")
    Integer guest;

    @Column(name = "contract_table")
    Integer table;

    @Column(name = "contract_totalcost")
    Double totalcost;

    @Column(name = "contract_prepay")
    Double prepay;

    @Column(name = "contract_status")
    String status;

    @Column(name = "contract_paymentstatus")
    String paymentstatus;

    @Column(name = "contract_organizdate")
    LocalDateTime organizdate;

    @Column(name = "contract_description")
    String description;

    @Column(name = "contract_custname")
    String custname;

    @Column(name = "contract_custphone")
    String custphone;

    @Column(name = "contract_custmail")
    String custmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User users;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location locations;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event events;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    Menu menus;

    @OneToMany(mappedBy = "contracts")
    List<StockRequest> listStockrequests;

    @OneToMany(mappedBy = "contract")
    List<PaymentHistory> listPaymentHistories;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
