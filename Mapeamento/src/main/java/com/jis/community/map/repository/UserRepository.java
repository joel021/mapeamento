package com.jis.community.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jis.community.map.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
 
	User findByEmail(String email);
	
	List<User> findByActive(int active);
	
	List<User> findByAllowedBy(String allowedBy);
	
}