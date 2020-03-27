package com.jis.community.map.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jis.community.map.model.Register;
import com.jis.community.map.model.User;
import com.jis.community.map.model.UserForm;
import com.jis.community.map.service.RegisterService;
import com.jis.community.map.service.UserService;

@Controller
public class MainController {

	 @Autowired
	 private UserService userService;

	 
	 @Autowired
	 private RegisterService registerService;
	 
	 @RequestMapping(value = "/public/get_registers/", method = RequestMethod.POST)
     public @ResponseBody List<Register> searchProblemByState(@RequestBody(required = true) Map<String, String> args) {
		 List<Register> list = new ArrayList<Register>();
		 
		 if(args.get("precision").equals("0") && args.get("city") != null) {
			 //cidade
			 list = registerService.findRegisterByTypeAndCity(args.get("type"), args.get("city"));
		 }else if(args.get("precision").equals("1") && args.get("state") != null) {
			 list = registerService.findRegisterByTypeAndState(args.get("type"), args.get("state"));
		 }else if(args.get("country") != null && args.get("precision").equals("2")){
			 list = registerService.findRegisterByTypeAndCountry(args.get("type"), args.get("country"));
		 }
		 return list;
	 }
	 
	 @RequestMapping(value= {"/private/main"}, method=RequestMethod.GET)
	 public ModelAndView adminG() {
		  ModelAndView model = new ModelAndView();
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  User user = userService.findUserByEmail(auth.getName());
		  
		  List<User> usersToActive = userService.findUserByActive(0);
		  
		  model.addObject("user", user);
		  model.addObject("userForm", new UserForm().getUserFormFrom(user));
		  model.addObject("usersToActive", usersToActive);
		  model.setViewName("/private/main");
		  return model;
	 }
	 
	 @RequestMapping(value= {"/private/registry_info"}, method=RequestMethod.GET)
	 public ModelAndView info() {
		  ModelAndView model = new ModelAndView();
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  User user = userService.findUserByEmail(auth.getName());
		  
		  model.addObject("msg", "Olá, "+user.getName());
		  model.setViewName("/private/registry_info");
		  return model;
	 }
	 
	 @RequestMapping(value= {"/", "/access_denied"}, method=RequestMethod.GET)
	 public ModelAndView main() {
		  ModelAndView model = new ModelAndView();
		  model.setViewName("/public/main");
		  return model;
	 }

	 @RequestMapping(value= {"/private/registry_info/"}, method=RequestMethod.POST)
	 public @ResponseBody int addRegistry(@RequestBody(required = true) Map<String, String> map) {
		  		  
		  Register register = new Register();
		  register.setLat(Float.parseFloat(map.get("lat")));
		  register.setLon(Float.parseFloat(map.get("lon")));
		  register.setCity(map.get("city"));
		  register.setState(map.get("state"));
		  register.setHamlet(map.get("hamlet"));
		  register.setCountry(map.get("country"));
		  register.setPostCode(map.get("postcode"));
		  register.setInfo(map.get("info"));
		  register.setSituation(map.get("situation"));
		  register.setType(map.get("types"));
		  register.setDate(new java.sql.Date(new Date().getTime()));
		  
		  registerService.saveRegister(register);
		  
		  return 200;
	 }
	 
	 @RequestMapping(value= {"/private/updateRegister/"}, method=RequestMethod.POST)
	 public @ResponseBody int updateRegister(@RequestBody(required = true) Map<String, String> args) {
		  
		 Register register = new Register();
		 register.setId(Long.parseLong(args.get("id")));
		 
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  User user = userService.findUserByEmail(auth.getName());
		 
		 if(args.get("id") != null && (user.getPrincipalRole().equals("ADMIN") || user.getPrincipalRole().equals("AGENT"))) {
			 register.setSituation(args.get("situation"));
			 register.setUpdatedBy(user.getEmail());
			 // outras atualizacoes
			 registerService.updateRegister(register);
			 return 200;
		 }
		  
		 return 500;
	 }
	 
	 @RequestMapping(value= {"/error"}, method=RequestMethod.GET)
	 public ModelAndView error() {
		  ModelAndView model = new ModelAndView();
		  
		  model.setViewName("/public/error");
		  return model;
	 }
}