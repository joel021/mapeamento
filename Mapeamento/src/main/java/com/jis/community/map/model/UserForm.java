package com.jis.community.map.model;

public class UserForm extends User{
	private String password2;

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public User getUser() {
		
		User user = new User();
		user.setEmail(getEmail());
		user.setName(getName());
		user.setLastName(getLastName());
		user.setPassword(getPassword());
		user.setPrincipalRole(getPrincipalRole());
		
		return user;
	}
	
	public UserForm getUserFormFrom(User user) {
		setEmail(user.getEmail());
		setName(user.getName());
		setLastName(user.getLastName());
		setPassword(user.getPassword());
		setPrincipalRole(user.getPrincipalRole());
	
		return this;
	}
}
