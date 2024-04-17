package com.dev.template_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name="person")
public class Person {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "cin")
	private String cin;

	@Column(name = "bithday")
	private java.time.LocalDate bithday;


	@ManyToMany
	@JoinTable(name = "region_person", joinColumns= {@JoinColumn(name = "person_id", referencedColumnName = "id")},  inverseJoinColumns = {@JoinColumn(name = "region_id",referencedColumnName = "id")})
	private java.util.List<Region> listRegion;
  
    	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setFirstname(String firstname){
		this.firstname = firstname;
	}
	public String getFirstname(){
		return this.firstname;
	}
	public void setLastname(String lastname){
		this.lastname = lastname;
	}
	public String getLastname(){
		return this.lastname;
	}
	public void setCin(String cin){
		this.cin = cin;
	}
	public String getCin(){
		return this.cin;
	}
	public void setBithday(java.time.LocalDate bithday){
		this.bithday = bithday;
	}
	public java.time.LocalDate getBithday(){
		return this.bithday;
	}
	public void setListRegion(java.util.List<Region> listRegion){
		this.listRegion = listRegion;
	}
	public java.util.List<Region> getListRegion(){
		return this.listRegion;
	}

}
