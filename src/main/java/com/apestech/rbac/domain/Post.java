package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 功能：岗位实体
 *
 * @author xul
 * @create 2017-12-15 11:09
 */

@Entity
@Table(name = "aut_posts")
@Data
public class Post implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "organization")
    private String organization;

    @Column(name = "department")
    private String department;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "aut_post_users",
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> assignedUsers;

    @ManyToMany(mappedBy = "assignedPosts", fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<Role>();

    @ManyToMany(mappedBy = "assignedPosts", fetch = FetchType.EAGER)
    private List<Organization> organizations = new ArrayList<Organization>();
}