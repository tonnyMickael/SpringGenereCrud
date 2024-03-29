package com.dev.template_crud.model.tmp;

import java.util.List;

import jakarta.persistence.*;

public class Tmp {
	@Entity
	public class Person {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne(mappedBy = "person")
	    private Address address;
	    
	    @OneToMany(mappedBy = "person")
	    private List<Address> listAddress;

	    // Other properties and methods
	}

	@Entity
	public class Address {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    @JoinColumn(name = "person_id")
	    private Person person1;
	    
	    @ManyToOne
	    @JoinTable(
	        name = "person_address", 
	        joinColumns = {@JoinColumn(name = "address_id")}, 
	        inverseJoinColumns = {@JoinColumn(name = "person_id")})
	    private Person person;

	    // Other properties and methods
	}

}
