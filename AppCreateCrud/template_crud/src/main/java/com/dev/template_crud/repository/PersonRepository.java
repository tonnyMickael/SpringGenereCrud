package com.dev.template_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.*;
import com.dev.template_crud.model.*;

public interface PersonRepository extends JpaRepository<Person, Integer>{
	Page<Person> findAll(Pageable pageable);
}
