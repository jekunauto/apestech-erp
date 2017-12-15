package com.apestech.rbac.repository;

import com.apestech.rbac.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByName(String name);

}
