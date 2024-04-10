package com.dev.template_crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.template_crud.model.Role;
import com.dev.template_crud.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }
}
