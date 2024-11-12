package com.springboot.obbm.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    @Column(name = "permission_name")
    String name;
    @Column(name = "permission_description")
    String description;

    @ManyToOne
    @JoinColumn(name = "pergroup_name")
    PerGroup pergroups;

    @OneToMany(mappedBy = "permissions")
    List<UserGroupPermission> listUGPs;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
