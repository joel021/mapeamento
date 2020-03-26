package com.jis.community.map.model;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {
	 
	@Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private long id;
	
	 @Column(name = "email")
	 private String email;
	 
	 @Column(name = "password")
	 private String password;
	 
	 @Column(name = "active")
	 private int active;
	 
	 private String name, lastName;
	 
	 private String code;
	 
	 @OneToMany(mappedBy = "user", targetEntity=Register.class, cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	 private Set<Register> registers;
	 
	 @ManyToMany(cascade=CascadeType.ALL)
	 @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	 private Set<Role> roles;
	
	 private String principalRole;
	 
	 private String allowedBy;
	 
	 public String getEmail() {
		 return email;
	 }
	
	 public void setEmail(String email) {
	  this.email = email;
	 }
	
	
	 public String getPassword() {
	  return password;
	 }
	
	 public void setPassword(String password) {
	  this.password = password;
	 }
	
	 public int getActive() {
	  return active;
	 }
	
	 public void setActive(int active) {
	  this.active = active;
	 }
	
	 public Set<Role> getRoles() {
	  return roles;
	 }
	
	 public void setRoles(Set<Role> roles) {
	  this.roles = roles;
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<Register> getRegisters() {
		return registers;
	}

	public void setRegisters(Set<Register> registers) {
		this.registers = registers;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAllowedBy() {
		return allowedBy;
	}

	public void setAllowedBy(String allowedBy) {
		this.allowedBy = allowedBy;
	}

	public String getPrincipalRole() {
		return principalRole;
	}

	public void setPrincipalRole(String principalRole) {
		this.principalRole = principalRole;
	}
 
}
