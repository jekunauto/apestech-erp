package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="aut_roles")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	/**
	 * These are the members assined to this role. Initially this would be an 
	 * empty collection.
	 */
	@ManyToMany
	@JoinTable(name="aut_role_posts",
			joinColumns={@JoinColumn(name="role_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="post_id", referencedColumnName="id")}
	)
	private Set<Post> assignedPosts;
	
	/**
	 * These are all direct descendants of this role.
	 */
	@ManyToMany
	@JoinTable(name="aut_roles_inheritance",
			joinColumns={@JoinColumn(name="role_parent", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="role_child", referencedColumnName="id")})
	private Set<Role> descendants;

	/**
	 * These are all direct ascendants of this role.
	 */
	@ManyToMany
	@JoinTable(name="aut_roles_inheritance",
			joinColumns={@JoinColumn(name="role_child", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="role_parent", referencedColumnName="id")})
	private Set<Role> ascendants;
	
	/**
	 * Here we can access all DSD in which this role appears.
	 */
	@ManyToMany(mappedBy="roles")
	private Set<DynamicSeparationDuty> dynamicSeparations;
	
	@ManyToMany
	@JoinTable(name="aut_role_permissions",
			joinColumns={@JoinColumn(name="role_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="permission_id", referencedColumnName="id")})
	private Set<Permission> permissions;

	/**
	 * A role is equal with another role only by equality of name
	 * or primary key.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Role) {
			Role role = Role.class.cast(obj);
			
			return role.getId() == this.getId() ||
				this.getName().equalsIgnoreCase(role.getName());
		}
		
		return super.equals(obj);
	}
}