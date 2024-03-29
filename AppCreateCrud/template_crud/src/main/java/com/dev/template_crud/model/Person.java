package com.dev.template_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name="person")
public class Person {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "firstname", nullable = false)
	private String firstname;

	@Column(name = "lastname", nullable = false)
	private String lastname;

	@Column(name = "birthday", nullable = false)
	private java.time.LocalDate birthday;

	@Column(name = "cin", nullable = false)
	private String cin;

  
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
	public void setBirthday(java.time.LocalDate birthday){
		this.birthday = birthday;
	}
	public java.time.LocalDate getBirthday(){
		return this.birthday;
	}
	public void setCin(String cin){
		this.cin = cin;
	}
	public String getCin(){
		return this.cin;
	}

}
