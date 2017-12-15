package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="aut_dsd")
@Data
public class DynamicSeparationDuty implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="cardinality")
	private int cardinality;

	@ManyToMany
	@JoinTable(name="aut_dsd_roles",
			joinColumns={@JoinColumn(name="dsd_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="id")})
	private Set<Role> roles;

}