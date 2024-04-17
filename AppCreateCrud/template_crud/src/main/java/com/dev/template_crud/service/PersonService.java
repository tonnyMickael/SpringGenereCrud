package com.dev.template_crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import com.dev.template_crud.model.*;
import com.dev.template_crud.repository.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;

	public Page<Person> listPaginated(Pageable pageable){
		return this.personRepository.findAll(pageable);
	}
	
	public int count() {
		return (int)this.personRepository.count();
	}

	public Person add(Person person) {
		return this.personRepository.save(person);
	}

	public List<Person> fetch() {
		return this.personRepository.findAll();
	} 

	public Person update (Person person, Integer id) {
		Person personUpdated = new Person();
		personUpdated.setId(id);
		personUpdated.setFirstname(person.getFirstname());
		personUpdated.setLastname(person.getLastname());
		personUpdated.setCin(person.getCin());
		personUpdated.setBithday(person.getBithday());
		personUpdated.setListRegion(person.getListRegion());		return this.personRepository.save(personUpdated);
	}

	public void delete(Integer id) {
		this.personRepository.deleteById(id);
	}

	public Person detail (Integer id) {
		Person personDB = personRepository.findById(id).get();
		return personDB;
	}

} 
