package com.dev.template_crud.repository;


import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.template_crud.model.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long>{
	Page<Produit> findAll(Pageable pageable);
}
