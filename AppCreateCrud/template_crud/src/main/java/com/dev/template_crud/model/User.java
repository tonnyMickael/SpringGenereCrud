package com.dev.template_crud.model;



import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User{
    @Id
    @Column(nullable =false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    
    @ManyToMany
    @JoinTable(name = "users_roles" ,joinColumns ={@JoinColumn(name = "user_id",referencedColumnName = "id")},inverseJoinColumns ={@JoinColumn(name = "role_id",referencedColumnName = "id")} )
    
    private java.util.List<Role> listRole;
    

    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

   



    /**
     * @return java.util.List<Role> return the listrole
     */
    public java.util.List<Role> getListRole() {
        return this.listRole;
    }

    /**
     * @param listrole the listrole to set
     */
    public void setListRole(java.util.List<Role> listRole) {
        this.listRole = listRole;
    }

}
