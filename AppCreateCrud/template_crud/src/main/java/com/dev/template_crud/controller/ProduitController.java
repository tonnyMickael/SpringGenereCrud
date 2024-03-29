package com.dev.template_crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.template_crud.model.Produit;
import com.dev.template_crud.model.*;
import com.dev.template_crud.service.ProduitService;
import com.dev.template_crud.service.*;

@Controller
@RequestMapping("/produit")
public class ProduitController {
	
	@Autowired
	private ProduitService produitService;
	
	@Autowired
	private TagService tagService;
	
//    @GetMapping("/list")
//    public String listProduits(Model theModel) {
//        List < Produit > theProduits = produitService.fetch();
//        theModel.addAttribute("produits", theProduits);
//        return "produit-list";
//    }
    
    @GetMapping("/add")
    public String showFormForAdd(Model theModel) {
        Produit produit = new Produit();
        theModel.addAttribute("produit", produit);
        theModel.addAttribute("listTag", tagService.fetch());
        return "produit-form";
    }
    
    @PostMapping("/add")
    public String save(@ModelAttribute("produit") Produit produit, @RequestParam("tag_id") Long tag_id) {
    	Tag tagSelected = tagService.detail(tag_id);
    	produit.setTag(tagSelected);
        produitService.add(produit);
        
        return "redirect:/produit/list";
    }
    
    private boolean isAnyFieldNullOrEmpty(Produit produit) {
    	if (produit.getDesignation() == null || produit.getDesignation().trim().isEmpty() ||  produit.getPrix() == 0 || produit.getTag() == null) {
    		return true;
		} else {
			return false;
		}
    }    
    
    @PostMapping("/update/{id}")
    public String update(Model theModel, @PathVariable("id") long theId, @ModelAttribute("produit") Produit produit,@RequestParam("tag_id") Long tag_id) throws Exception {
        produit.setId(theId);
    	Tag tagSelected = tagService.detail(tag_id);
    	produit.setTag(tagSelected);
        // Check if any field of the produit object is null or empty
        if (isAnyFieldNullOrEmpty(produit)) {
            String errorMessage = "Some fields are null or empty.";
            theModel.addAttribute("errorMessage", errorMessage);
            Produit theProduit = produitService.detail(theId);
            theModel.addAttribute("produit", theProduit);
            return "redirect:/produit/list";
        }
        else {
        	this.produitService.update(produit, theId);
        }
        return "redirect:/produit/list";
    }
    
    @GetMapping("/update/{id}")
    public String showFormForUpdate(Model theModel, @PathVariable("id") long produitId) {
        try {
            Produit produit = produitService.detail(produitId);
            theModel.addAttribute("produit", produit);
            theModel.addAttribute("listTag",tagService.fetch());
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "produit-edit-form";
    }
    
    @GetMapping("/detail/{id}")
    public String showDetail(Model theModel, @PathVariable("id") long produitId) {
        try {
            Produit produit = produitService.detail(produitId);
            theModel.addAttribute("produit", produit);
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "produit-detail";
    }
    
    @GetMapping(value = "/list")
    public String getProduits(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
    	int ROW_PER_PAGE = 3;
    	
    	Pageable pageable = PageRequest.of(pageNumber - 1, ROW_PER_PAGE);
        Page<Produit> listProduit = produitService.listPaginated(pageable);
        
        int count = produitService.count();
        boolean hasPrev = pageNumber > 0;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("produits", listProduit.getContent());
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "produit-list";
    }   

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long theId) {
        produitService.delete(theId);
        return "redirect:/produit/list";
    }
}
