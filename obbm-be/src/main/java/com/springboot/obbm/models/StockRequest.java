package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name = "stockrequest")
public class StockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockrequestId;

    @Column(name = "stockrequest_quantity")
    private int quantity;

    @Column(name = "stockrequest_approval")
    private String approval;

    @Column(name = "stockrequest_requestdate")
    private Date requestdate;

    @Column(name = "stockrequest_receiveddate ")
    private Date receiveddate ;

    @Column(name = "stockrequest_status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contracts;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredients;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
