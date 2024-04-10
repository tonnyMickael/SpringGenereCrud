package com.dev.template_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.template_crud.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>{
    Role findByRole(String role);
}
