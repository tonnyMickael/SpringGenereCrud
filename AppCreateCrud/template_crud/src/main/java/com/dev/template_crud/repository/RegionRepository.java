package com.dev.template_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.*;
import com.dev.template_crud.model.*;

public interface RegionRepository extends JpaRepository<Region, Integer>{
	Page<Region> findAll(Pageable pageable);
}
