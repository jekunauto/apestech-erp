package com.apestech.scm.repository;

import com.apestech.scm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

	
}
