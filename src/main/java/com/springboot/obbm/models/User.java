package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    @Column(name = "user_account")
    String username;

    @Column(name = "user_password")
    String password;

    @Column(name = "user_fullname")
    String fullName;

    @Column(name = "user_email")
    String email;

    @Column(name = "user_phone")
    String phone;

    @Column(name = "user_image")
    String image;

    @Column(name = "user_citizenidentity")
    String citizenIdentity;

    @Column(name = "user_Dob")
    LocalDate dob;

    @Column(name = "user_status")
    boolean status;

    @Column(name = "isdeleted")
    boolean isDeleted;

    @OneToMany(mappedBy = "users")
    Set<Location> listLocation;

    @OneToMany(mappedBy = "users")
    Set<Menu> listMenu;

    @OneToMany(mappedBy = "users")
    Set<Contract> listContract;

    @ManyToMany
    Set<Role> roles;
}
