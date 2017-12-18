package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="aut_users")
@Data
public class User implements Serializable {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name; //名称

	private String userId; //操作帐号

	private String password; //密码
	
	@ManyToMany(mappedBy="assignedUsers", fetch=FetchType.EAGER)
	private List<Post> posts = new ArrayList<Post>();

}