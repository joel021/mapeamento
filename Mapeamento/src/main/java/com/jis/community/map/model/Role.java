package com.jis.community.map.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {
	 
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 @Column(name="role_id")
	 private long id;
	 
	 @Column(name="role")
	 private String role;
	
	 public String getRole() {
	  return role;
	 }
	
	 public void setRole(String role) {
	  this.role = role;
	 }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	 
	 
}