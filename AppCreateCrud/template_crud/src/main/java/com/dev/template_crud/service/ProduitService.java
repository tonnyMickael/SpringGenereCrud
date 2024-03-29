package com.dev.template_crud.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.template_crud.model.Produit;
import com.dev.template_crud.repository.ProduitRepository;

@Service
public class ProduitService {
	
	@Autowired
	private ProduitRepository produitRepository;

	
	public Produit add(Produit produit) {
		return this.produitRepository.save(produit);
	}
	
	public Page<Produit> listPaginated(Pageable pageable){
		return this.produitRepository.findAll(pageable);
	}
	
	public int count() {
		return (int)this.produitRepository.count();
	}
	
	public List<Produit> fetch() {
		return this.produitRepository.findAll();
	} 
	
	public void delete(Long id) {
		this.produitRepository.deleteById(id);
	}
	
	public Produit update (Produit produit, Long id) {
		Produit produitUpdated = new Produit();
		produitUpdated.setId(id);
		produitUpdated.setDesignation(produit.getDesignation());
		produitUpdated.setPrix(produit.getPrix());
		produitUpdated.setTag(produit.getTag());
		return this.produitRepository.save(produitUpdated);
	}
	
	public Produit detail (Long id) {
		Produit produitDB = produitRepository.findById(id).get();
		return produitDB;
	}
} 
