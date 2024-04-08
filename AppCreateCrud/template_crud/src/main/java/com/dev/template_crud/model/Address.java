package com.dev.template_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name="address")
public class Address {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "street", nullable = false)
	private String street;

	@Column(name = "zipcode", nullable = false)
	private String zipcode;

<<<<<<< Updated upstream
=======

	@ManyToOne
	@JoinColumn(name = "person_id", foreignKey = @ForeignKey(name="fk_person_address"),nullable = false)
	private Person person;
>>>>>>> Stashed changes
  
    	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setStreet(String street){
		this.street = street;
	}
	public String getStreet(){
		return this.street;
	}
	public void setZipcode(String zipcode){
		this.zipcode = zipcode;
	}
	public String getZipcode(){
		return this.zipcode;
	}
<<<<<<< Updated upstream
=======
	public void setPerson(Person person){
		this.person = person;
	}
	public Person getPerson(){
		return this.person;
	}
>>>>>>> Stashed changes

}
