package com.dev.template_crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.template_crud.model.Tag;
import com.dev.template_crud.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	public List<Tag> fetch() {
		return this.tagRepository.findAll();
	}
	
	public Tag detail(Long id) {
		return this.tagRepository.findById(id).orElse(null);
	}
}
