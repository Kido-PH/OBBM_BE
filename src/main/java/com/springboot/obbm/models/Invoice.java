package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceId;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contracts;

    @Column(name = "invoice_issuedate")
    private Date invoiceIssueDate;

    @Column(name = "invoice_duedate")
    private Date invoiceDueDate;

    @Column(name = "invoice_payment")
    private String invoicePayment;

    @Column(name = "invoice_totalamount")
    private double invoiceTotalAmount;

    @Column(name = "invoice_content")
    private String invoiceContent;

    @Column(name = "invoice_isstatus")
    private Boolean invoiceIsStatus;


}