package com.dev.template_crud.model;

import jakarta.persistence.*;

@Entity
@Table(name="produit")
public class Produit {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "designation")
	private String designation;

	@Column(name = "prix")
	private double prix;

	@OneToOne
	@JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_produit_tag"))
	private Tag tag;
    
    public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	public void setId(Long id){
		this.id = id;
	}
	public Long getId(){
		return this.id;
	}
	public void setDesignation(String designation){
		this.designation = designation;
	}
	public String getDesignation(){
		return this.designation;
	}
	public void setPrix(double prix){
		this.prix = prix;
	}
	public double getPrix(){
		return this.prix;
	}

}
