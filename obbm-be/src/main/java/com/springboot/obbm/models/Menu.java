package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId;

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event events;

    @OneToMany(mappedBy = "menus")
    private List<MenuDish> listMenuDish;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
