package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 功能：组织实体
 *
 * @author xul
 * @create 2017-12-15 13:44
 */
@Entity
@Table(name = "aut_organizations")
@Data
public class Organization implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private int type; //门店、公司、仓库、网点

    @Column(name = "purpose")
    private int purpose; //操作、所属、查询

    @ManyToMany
    @JoinTable(name="aut_organizations_posts",
            joinColumns={@JoinColumn(name="org_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="post_id", referencedColumnName="id")}
    )
    private Set<Post> assignedPosts;
}
