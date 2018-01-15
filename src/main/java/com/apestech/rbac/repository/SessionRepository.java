package com.apestech.rbac.repository;


import com.apestech.rbac.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SessionRepository extends JpaRepository<Session, Integer>, JpaSpecificationExecutor<Session> {

}
