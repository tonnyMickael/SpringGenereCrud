package com.dev.template_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.template_crud.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email); 
    User findByName(String name);
}
