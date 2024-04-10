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
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonService personService;

    @Autowired
	private RegionService regionService;
	

    @GetMapping(value = "/list")
    public String listPersons(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
    	int ROW_PER_PAGE = 3;
    	
    	Pageable pageable = PageRequest.of(pageNumber - 1, ROW_PER_PAGE);
        Page<Person> listPerson = personService.listPaginated(pageable);
        
        int count = personService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("persons", listPerson.getContent());
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "person-list";
    }  

    @GetMapping("/add")
    public String showFormForAdd(Model theModel) {
        Person person = new Person();
        theModel.addAttribute("person", person);
        theModel.addAttribute("listRegion", regionService.fetch());
        return "person-form";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("person") Person person , @RequestParam("region_id") Integer region_id) {
        Region childSelected = regionService.detail(region_id);
		person.setListRegion(Arrays.asList(childSelected));


        personService.add(person);

        return "redirect:/person/list";
    }

    private boolean isAnyFieldNullOrEmpty(Person person) {
    	if (person.getFirstname() == null || person.getFirstname().trim().isEmpty() || person.getLastname() == null || person.getLastname().trim().isEmpty() || person.getBirthday() == null || person.getCin() == null || person.getCin().trim().isEmpty()) {
    		return true;
		} else {
			return false;
		}
    } 

    @PostMapping("/update/{id}")
    public String update(Model theModel, @PathVariable("id") Integer theId, @ModelAttribute("person") Person person , @RequestParam("region_id") Integer region_id) throws Exception {
        person.setId(theId);
        Region childSelected = regionService.detail(region_id);
		person.setListRegion(Arrays.asList(childSelected));

        // Check if any field of the produit object is null or empty
        if (isAnyFieldNullOrEmpty(person)) {
            String errorMessage = "Some fields are null or empty.";
            theModel.addAttribute("errorMessage", errorMessage);
            Person personDetail = personService.detail(theId);
            theModel.addAttribute("person", personDetail);
            return "redirect:/person/list";
        }
        else {
        	this.personService.update(person, theId);
        }
        return "redirect:/person/list";
    }
    
    @GetMapping("/update/{id}")
    public String showFormForUpdate(Model theModel, @PathVariable("id") Integer theId) {
        try {
            Person person = personService.detail(theId);
            theModel.addAttribute("person", person);
            theModel.addAttribute("listRegion", regionService.fetch());
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "person-edit-form";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(Model theModel, @PathVariable("id") Integer id) {
        try {
            Person person = personService.detail(id);
            theModel.addAttribute("person", person);
        } catch (Exception ex) {
            theModel.addAttribute("errorMessage", ex.getMessage());
        }
        return "person-detail";
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable("id") Integer theId) {
        personService.delete(theId);
        return "redirect:/person/list";
    }
    @GetMapping("/detail/{id}/region/add")
	public String addRegionInDetailPerson(Model theModel, @PathVariable("id") Integer id) {
		Region region = new Region();
		try {
			theModel.addAttribute("region", region);
			theModel.addAttribute("listRegion", this.regionService.fetch());
			Person person = personService.detail(id);
			theModel.addAttribute("person", person);
		} catch (Exception ex) {
			theModel.addAttribute("errorMessage", ex.getMessage());
		}
		return "person-region-form";
	}

	@PostMapping("/detail/region/add")
	public String saveRegionInDetailPerson(@ModelAttribute("region") Region region,@RequestParam("person_id") Integer person_id) {
		try {
			Person person = this.personService.detail(person_id);
			Region regionObj = this.regionService.detail(region.getId());
			person.getListRegion().add(regionObj); 
			this.personService.add(person);
			return "redirect:/person/detail/"+person_id;
		} catch (Exception ex) {
			ex.getMessage();
		}
		return null;
	}

    @GetMapping("/detail/region/update/{person_id}/{region_id}")
	public String updateRegionInDetailForm (Model theModel, @PathVariable("person_id") Integer person_id,@PathVariable("region_id") Integer region_id) {
		try {
			Region region = regionService.detail(region_id);
            Person person = personService.detail(person_id);
			theModel.addAttribute("region", region);
			theModel.addAttribute("person", person);
			theModel.addAttribute("listRegion", this.regionService.fetch());
		} catch (Exception ex) {
			theModel.addAttribute("errorMessage", ex.getMessage());
		}
		return "person-region-edit-form";
	}

    @PostMapping("/detail/region/update")
	public String updateRegionInDetailPerson(@ModelAttribute("region") Region region,@RequestParam("person_id") Integer person_id,@RequestParam("region_id") Integer region_id) {
		try {
            Person person = this.personService.detail(person_id);
            List<Region> listRegion= person.getListRegion();
            Region regionUpdated = this.regionService.detail(region.getId());

            int indexToRemove = -1;
            for (int i = 0; i < listRegion.size(); i++) {
                if (listRegion.get(i).getId() == (region_id)) {
                    indexToRemove = i;
                    break;
                }
            }
            if (indexToRemove != -1) {
                listRegion.set(indexToRemove, regionUpdated);
                person.setListRegion(listRegion);
                this.personService.update(person, person.getId());
                return "redirect:/person/detail/"+person_id;
            }
		} catch (Exception ex) {
			ex.getMessage();
		}
		return null;
	}

    @GetMapping("/detail/region/delete/{person_id}/{id}")
	public String deleteRegionInDetail(@PathVariable("person_id") Integer person_id, @PathVariable("id") Integer theId) {
        try {
            Region region = this.regionService.detail(theId);
            Person person = this.personService.detail(person_id);
            List<Region> listRegion = person.getListRegion();
            Region regionFromList =  listRegion.stream().filter(regionIndex -> regionIndex.getId() == region.getId()).findFirst().orElse(null);
            listRegion.remove(regionFromList);
            person.setListRegion(listRegion);
            this.personService.update(person, person.getId());
            return "redirect:/person/detail/"+person_id;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
	}

}
