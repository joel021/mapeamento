package com.jis.community.map.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jis.community.map.model.Register;
import com.jis.community.map.service.RegisterService;

@RestController
@RequestMapping(value="/api")
public class RestAppController {

	
	@Autowired
	RegisterService registerService;
	
	// por dada de atualização no formato string
	@GetMapping("/getRegisters")
	public List<Register> getRegisters(@RequestParam(value="type") String type, 
			@RequestParam(value="precision") String precision, @RequestParam(value="state") String state
			,@RequestParam(value="city") String city, @RequestParam(value="country") String country)
	
	{
		
		List<Register> list = null;
		 
		 if(precision.equals("0") && city != null) {
			 //cidade
			 list = registerService.findRegisterByTypeAndCity(type, city);
		 }else if(precision.equals("1")) {
			 list = registerService.findRegisterByTypeAndState(type, state);
		 }else {
			 list = registerService.findRegisterByTypeAndCountry(type, country);
		 }
		 return list;
	}
}
