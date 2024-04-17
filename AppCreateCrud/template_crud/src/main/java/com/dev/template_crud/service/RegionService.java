package com.dev.template_crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import com.dev.template_crud.model.*;
import com.dev.template_crud.repository.RegionRepository;

@Service
public class RegionService {
	
	@Autowired
	private RegionRepository regionRepository;

	public Page<Region> listPaginated(Pageable pageable){
		return this.regionRepository.findAll(pageable);
	}
	
	public int count() {
		return (int)this.regionRepository.count();
	}

	public Region add(Region region) {
		return this.regionRepository.save(region);
	}

	public List<Region> fetch() {
		return this.regionRepository.findAll();
	} 

	public Region update (Region region, Integer id) {
		Region regionUpdated = new Region();
		regionUpdated.setId(id);
		regionUpdated.setName(region.getName());
		regionUpdated.setListPerson(region.getListPerson());		return this.regionRepository.save(regionUpdated);
	}

	public void delete(Integer id) {
		this.regionRepository.deleteById(id);
	}

	public Region detail (Integer id) {
		Region regionDB = regionRepository.findById(id).get();
		return regionDB;
	}

} 
