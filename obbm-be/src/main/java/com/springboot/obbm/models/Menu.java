package com.springboot.obbm.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @Column(name = "menu_datecreate")
    private Date menuDateCreate;

    @Column(name = "menu_type")
    private String menuType;

    @OneToMany(mappedBy = "menus")
    private Set<MenuDish> listMenuDish;
}
