package com.apestech.rbac.repository;


import com.apestech.rbac.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
