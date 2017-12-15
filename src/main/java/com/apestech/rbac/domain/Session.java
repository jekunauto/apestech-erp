package com.apestech.rbac.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="aut_sessions")
@Data
public class Session {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@Column(name="active")
	private boolean active;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="remote_session")
	private String remoteSession;

	/**
	 * These are all roles activated within the current session.
	 */
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="aut_session_posts",
			joinColumns={@JoinColumn(name="session_id", referencedColumnName="id")},
			inverseJoinColumns={@JoinColumn(name="post_id", referencedColumnName="id")}
	)
	private Set<Role> sessionPosts;


}