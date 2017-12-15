package com.apestech.rbac.repository;

import com.apestech.rbac.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {

}
