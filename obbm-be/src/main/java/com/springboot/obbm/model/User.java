package com.springboot.obbm.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    @Column(name = "user_account", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @Column(name = "user_password")
    String password;

    @Column(name = "user_fullname")
    String fullname;

    @Column(name = "user_email")
    String email;

    @Column(name = "user_phone")
    String phone;

    @Column(name = "user_image")
    String image;

    @Column(name = "user_citizenidentity")
    String citizenIdentity;

    @Column(name = "user_dob")
    LocalDate dob;

    @OneToMany(mappedBy = "users")
    List<Location> listLocation;

    @OneToMany(mappedBy = "users")
    List<Menu> listMenu;

    @OneToMany(mappedBy = "users")
    List<Contract> listContract;

    @ManyToMany
    Set<Role> roles;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
