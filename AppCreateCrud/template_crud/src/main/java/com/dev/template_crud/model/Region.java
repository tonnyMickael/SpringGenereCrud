package com.dev.template_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name="region")
public class Region {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;


	@ManyToMany(mappedBy= "listRegion", cascade = CascadeType.ALL)
		private java.util.List<Person> listPerson;
  
    	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setListPerson(java.util.List<Person> listPerson){
		this.listPerson = listPerson;
	}
	public java.util.List<Person> getListPerson(){
		return this.listPerson;
	}

}
