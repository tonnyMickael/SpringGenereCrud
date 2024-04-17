package com.dev.template_crud.controller;

import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.dev.template_crud.model.*;
import com.dev.template_crud.service.*;

@Controller
@RequestMapping("/region")
public class RegionController {
	
	@Autowired
	private RegionService regionService;

    @Autowired
	private PersonService personService;
	

    @GetMapping(value = "/list")
    public String listRegions(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
    	int ROW_PER_PAGE = 3;
    	
    	Pageable pageable = PageRequest.of(pageNumber - 1, ROW_PER_PAGE);
        Page<Region> listRegion = regionService.listPaginated(pageable);
        
        int count = regionService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("regions", listRegion.getContent());
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "region-list";
    }  

    @GetMapping("/add")
    public String showFormForAdd(Model theModel) {
        Region region = new Region();
        theModel.addAttribute("region", region);
        theModel.addAttribute("listPerson", personService.fetch());
        return "region-form";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("region") Region region ) {
        

        regionService.add(region);

        return "redirect:/region/list";
    }

    private boolean isAnyFieldNullOrEmpty(Region region) {
    	if (region.getName() == null || region.getName().trim().isEmpty()|| region.getListPerson() == null) {
    		return true;
		} else {
			return false;
		}
    } 

    @PostMapping("/update/{id}")
    public String update(Model theModel, @PathVariable("id") Integer theId, @ModelAttribute("region") Region region ) throws Exception {
        region.setId(theId);
        
        // Check if any field of the produit object is null or empty
        if (isAnyFieldNullOrEmpty(region)) {
            String errorMessage = "Some fields are null or empty.";
            theModel.addAttribute("errorMessage", errorMessage);
            Region regionDetail = regionService.detail(theId);
            theModel.addAttribute("region", regionDetail);
            return "redirect:/region/list";
        }
        else {
        	this.regionService.update(region, theId);
        }
        return "redirect:/region/list";
    }
    
    @GetMapping("/update/{id}")
    public String showFormForUpdate(Model theModel, @PathVariable("id") Integer theId) {
        try {
            Region region = regionService.detail(theId);
            theModel.addAttribute("region", region);
            theModel.addAttribute("listPerson", personService.fetch());
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "region-edit-form";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model theModel, @PathVariable("id") Integer id) {
        try {
            Region region = regionService.detail(id);
            theModel.addAttribute("region", region);
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "region-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegion(@PathVariable("id") Integer theId) {
        regionService.delete(theId);
        return "redirect:/region/list";
    }
    
}
