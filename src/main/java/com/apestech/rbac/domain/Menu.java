package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "aut_menus")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    private String icon; //图标

    @Column(name = "link")
    private String link; //链接

    @Column(name = "flow_id")
    private String flowId; //流程

    @Column(name = "terminal")
    private String terminal; //终端

    private boolean valid; //有效

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Menu> children;

    @OneToMany(mappedBy="menu")
    private List<Permission> permissions;
}