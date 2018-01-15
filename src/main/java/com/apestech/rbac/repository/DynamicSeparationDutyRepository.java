package com.apestech.rbac.repository;


import com.apestech.rbac.domain.DynamicSeparationDuty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DynamicSeparationDutyRepository extends JpaRepository<DynamicSeparationDuty, Integer>, JpaSpecificationExecutor<DynamicSeparationDuty> {

}
