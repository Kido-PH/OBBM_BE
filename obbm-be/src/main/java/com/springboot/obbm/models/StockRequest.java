package com.springboot.obbm.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name = "stockrequest")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stockrequestId")
public class StockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer stockrequestId;

    @Column(name = "stockrequest_quantity")
    Integer quantity;

    @Column(name = "stockrequest_approval")
    String approval;

    @Column(name = "stockrequest_requestdate")
    Date requestdate;

    @Column(name = "stockrequest_receiveddate ")
    Date receiveddate ;

    @Column(name = "stockrequest_status")
    String status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    Contract contracts;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredients;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
