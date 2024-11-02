package com.springboot.obbm.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.springboot.obbm.util.StringFieldTrimmer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "menu")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "contractId")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer menuId;

    @Column(name = "menu_name")
    String name;

    @Column(name = "menu_description")
    String description;

    @Column(name = "menu_totalcost")
    Double totalcost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User users;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event events;

    @OneToMany(mappedBy = "menus")
    List<MenuDish> listMenuDish;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void trimFields(){
        StringFieldTrimmer.trimAndNormalizeStringFields(this);
    }
}
