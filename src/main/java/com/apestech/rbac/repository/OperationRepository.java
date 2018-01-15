package com.apestech.rbac.repository;

import com.apestech.rbac.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperationRepository extends JpaRepository<Operation, Integer>, JpaSpecificationExecutor<Operation> {

}
