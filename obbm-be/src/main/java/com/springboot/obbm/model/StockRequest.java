package com.springboot.obbm.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@Entity(name = "stockrequest")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    LocalDateTime requestdate;

    @Column(name = "stockrequest_receiveddate ")
    LocalDateTime receiveddate ;

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
