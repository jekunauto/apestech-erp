package com.apestech.scm.repository;

import com.apestech.scm.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Integer> {

	
}
