package com.apestech.rbac.repository;

import com.apestech.rbac.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Integer> {

}
