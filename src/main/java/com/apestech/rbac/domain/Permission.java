package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="aut_permissions")
@Data
public class Permission implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;

	@ManyToOne
	@JoinColumn(name="operation_id", referencedColumnName="id")
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="menu_id", referencedColumnName="id")
	private Menu menu;
	
	@ManyToMany(cascade={CascadeType.ALL}, mappedBy="permissions")
	private Set<Role> roles;
	
	/**
	 * Permissions equality is determined based on name or permision i.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Permission) {
			Permission perms = Permission.class.cast(obj);
			
			return perms.getId() == this.getId() || 
				perms.getName().equalsIgnoreCase(this.getName());
		}
		
		return super.equals(obj);
	}
}