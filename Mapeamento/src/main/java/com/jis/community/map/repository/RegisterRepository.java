package com.jis.community.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jis.community.map.model.Register;

@Repository("registerRepository")
public interface RegisterRepository extends JpaRepository<Register, Long>{
	

	List<Register> findByTypeAndCity(String type, String city);
	
	List<Register> findByTypeAndState(String type, String state);
	
	List<Register> findByTypeAndCountry(String type, String country);
}
