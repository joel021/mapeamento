package com.jis.community.map.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jis.community.map.model.Register;
import com.jis.community.map.repository.RegisterRepository;

@Service("registryService")
public class RegisterService {
	
	@Autowired
	private RegisterRepository registerRepository;
	
	
	public List<Register> findRegisterByTypeAndCity(String type, String city) {
		return registerRepository.findByTypeAndCity(type, city);
	}
	
	public List<Register> findRegisterByTypeAndState(String type, String state) {
		return registerRepository.findByTypeAndState(type, state);
	}
	public List<Register> findRegisterByTypeAndCountry(String type, String country) {
		return registerRepository.findByTypeAndCountry(type, country);
	}
	public void saveRegister(Register register) {
		registerRepository.save(register);
	}
	
	public void updateRegister(Register register) {
		
		Optional<Register> b = registerRepository.findById(register.getId());
		
		Register r = (b != null) ? b.get() : null;
		
		
		if(!register.getSituation().isEmpty() && r != null) {
			 r.setSituation(register.getSituation());
		}
		
		// outras atualizações
		if(r != null)
			registerRepository.save(r);
	}
}
