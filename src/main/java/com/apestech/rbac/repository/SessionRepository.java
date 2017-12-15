package com.apestech.rbac.repository;


import com.apestech.rbac.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Integer> {

}
