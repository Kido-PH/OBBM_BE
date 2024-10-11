package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "stockrequest")
public class StockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockRequestId;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contracts;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredients;

    @Column(name = "stockrequest_quantity")
    private int stockRequestQuantity;

    @Column(name = "stockrequest_approval")
    private String stockRequestApproval;

    @Column(name = "stockrequest_receiveddate ")
    private Date stockRequestReceivedDate ;

    @Column(name = "stockrequest_date")
    private Date stockRequestDate;

    @Column(name = "stockrequest_status")
    private String stockRequestStatus;


}
