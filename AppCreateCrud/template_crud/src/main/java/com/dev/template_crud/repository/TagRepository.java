package com.dev.template_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.template_crud.model.*;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

}
