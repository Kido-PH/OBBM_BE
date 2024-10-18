package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceId;

    @Column(name = "invoice_content")
    private String content;

    @Column(name = "invoice_duedate")
    private Date duedate;

    @Column(name = "invoice_payment")
    private String payment;

    @Column(name = "invoice_totalamount")
    private double totalamount;

    @Column(name = "invoice_status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contracts;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}