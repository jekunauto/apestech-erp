package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="aut_operations")
@Data
public class Operation implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
	private Integer id;

	@Column(name="name")
	private String name;

	@OneToMany(mappedBy="operation")
	private List<Permission> permissions;
}