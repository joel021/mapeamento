package com.jis.community.map.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jis.community.map.model.Role;
import com.jis.community.map.model.User;
import com.jis.community.map.repository.RoleRepository;
import com.jis.community.map.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service("userService")
public class UserService {
 
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private RoleRepository roleRespository;
	 
	 @Autowired
	 private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	 public User findUserByEmail(String email) {
		 return userRepository.findByEmail(email);
	 }

	 public User saveUser(User user) {
		 
		  user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		  user.setActive(0);
		  user.setCode(new Random().nextInt(100000)+"");
		  
		  Role userRole = roleRespository.findByRole(user.getPrincipalRole());
		  
		  user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		  return userRepository.save(user);
	 }
	 
	 public void updateUser(User user) {
		 
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 User authenticaded = userRepository.findByEmail(auth.getName());
		 
		 // mesmo que o usuário dê um jeito de inserir essas informações
		 user.setId(authenticaded.getId());
		 user.setRoles(authenticaded.getRoles());
		 
		 if(user.getActive() == 0 && user.getCode() != null)
			 if(user.getCode().equals(authenticaded.getCode()))
				 user.setActive(1);
		 
		 Role role = new Role();
		 role.setRole(authenticaded.getPrincipalRole());
		 
		 if(!user.getPrincipalRole().isEmpty()) {
			 if(!user.getPrincipalRole().equals(authenticaded.getRoles().iterator().next().getRole())) {
				 System.out.println("Setou role diferente");
				 user.setActive(0);
				 role.setRole(user.getPrincipalRole());
			 }
		 }
		 
		 if(user.getEmail() == null)
			 user.setEmail(authenticaded.getEmail());
		 
		 if(user.getName().isEmpty()) {
			 user.setName(authenticaded.getName());
		 }
		 
		 if(user.getLastName().isEmpty()) {
			 user.setLastName(authenticaded.getLastName());
		 }
		 
		 user.setCode(new Random().nextInt(100000)+"");
		 userRepository.save(user);
		
	 }
	 
	 
	 public boolean updateActive(long id, int active) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 User authenticaded = userRepository.findByEmail(auth.getName());
		 
		 if(authenticaded.getPrincipalRole().equals("ADMIN")) {
			 
			 if(active == 1) {
				 User user = userRepository.findById(id).get();
				 System.out.println("Ususário: "+user.getEmail());
				 
				 user.setActive(1);
				 user.setAllowedBy(authenticaded.getEmail());
				 userRepository.save(user);
				 
				 System.out.println("nome: "+user.getName());
			 }else {
				 userRepository.deleteById(id);
			 }
			 return true;
		 }
		 
		 return false;
	 }
	 
	 public List<User> findUserByActive(int active){
		 return userRepository.findByActive(active);
	 }
	 
	 public List<User> findUserByAllowedBy(String alowedBy){
		 return userRepository.findByAllowedBy(alowedBy);
	 }

	 public User findUserById(long id) {
		 return userRepository.findById(id).get();
	 }
}