package com.jis.community.map.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jis.community.map.model.User;
import com.jis.community.map.model.UserForm;
import com.jis.community.map.service.UserService;

@Controller
public class AutenController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value= {"/public/login"}, method=RequestMethod.GET)
	 public ModelAndView login() {
		  ModelAndView model = new ModelAndView();
		  
		  model.setViewName("/public/login");
		  return model;
	 }
	 
	 @RequestMapping(value= {"/public/signup"}, method=RequestMethod.GET)
	 public ModelAndView signup() {
		  ModelAndView model = new ModelAndView();
		  User userForm = new User(); 
		  model.addObject("userForm", userForm);
		  model.setViewName("/public/signup");
		  
		  return model;
	 }
	 
	 @RequestMapping(value= {"/public/confirm"}, method=RequestMethod.GET)
	 public ModelAndView confirmUser() {
		  ModelAndView model = new ModelAndView();
		  User user = new User();
		  model.addObject("user", user);
		  model.setViewName("/public/confirm");
		  
		  return model;
	 }
	 
	 @RequestMapping(value= {"/public/signup"}, method=RequestMethod.POST)
	 public ModelAndView createUser(@Valid UserForm userForm, BindingResult bindingResult) {
		  ModelAndView model = new ModelAndView("redirect:/public/confirm");
		  		  
		  User userExists = userService.findUserByEmail(userForm.getEmail());
		  
		  if(userExists != null) {
			  bindingResult.rejectValue("email", "error.user", "Este email já foi cadastrado!");
		  }else if(!userForm.getPassword().equals(userForm.getPassword2())) {
			  model.setViewName("/private/confirm");
			  bindingResult.rejectValue("password", "error.user", "Senhas não conferem.");
		  }if(bindingResult.hasErrors()) {
			  model.setViewName("/public/signup");
		  } else {
			  
			   userService.saveUser(userForm.getUser());
				
			   model.addObject("msg", "Usuário cadastrado com sucesso!");
			   model.addObject("user", new User());
			   model.setViewName("/public/confirm");
		  }
		  
		  return model;
	 }
	 
	 @RequestMapping(value= {"/public/confirm"}, method=RequestMethod.POST)
	 public ModelAndView requestConfirm(@Valid User user, BindingResult bindingResult) {
		  ModelAndView model = new ModelAndView("redirect:/public/login");
		  		  
		  User userExists = userService.findUserByEmail(user.getEmail());
		  
		  if(userExists == null) {
			  bindingResult.rejectValue("email", "error.user", "Este email não está cadastrado!");
		  }
		  if(bindingResult.hasErrors()) {
			  model.setViewName("/public/confirm");
		  } else if(userExists.getCode() == user.getCode()) {
			  
			   userExists.setActive(1);
			   userService.updateUser(userExists);
			   
			   model.addObject("msg", "Usuário confirmado com sucesso!");
			   model.addObject("user", new User());
			   model.setViewName("/public/login");
		  }
		  
		  return model;
	 }
	 
	 @RequestMapping(value= {"/private/update_user"}, method=RequestMethod.POST)
	 public ModelAndView update(@Valid UserForm userForm, BindingResult bindingResult) {
		  ModelAndView model = new ModelAndView("redirect:/private/main");
		  		 
		  if(bindingResult.hasErrors()) {
			  model.setViewName("/private/main");
			  bindingResult.rejectValue("name", "error.user", "Algo errado com seu preenchimento.");
		  }else if(!userForm.getPassword().equals(userForm.getPassword2())) {
			  model.setViewName("/private/main");
			  bindingResult.rejectValue("password", "error.user", "Senhas não conferem.");
			  
		  }else {
			  
			   userService.updateUser(userForm.getUser());
			   
			   model.addObject("msg", "Usuário atualizada com sucesso!");
			   model.addObject("user", new User());
			   model.setViewName("/private/main");
		  }
		  
		  return model;
	 }
	 
	 @RequestMapping(value= {"/private/activeOrNo/"}, method=RequestMethod.POST)
	 public @ResponseBody int addRegistry(@RequestBody(required = true) Map<String, String> args) {
		 System.out.println("\n\n---"+args.get("id")+", "+args.get("active"));
		 if(args.get("id") != null && args.get("active") != null) {
			 
			 boolean r = userService.updateActive(Long.parseLong(args.get("id")), Integer.parseInt(args.get("active")));
			 
			 if(r)
				 return 200;
		 }
		  
		  return 500; // erro
	 }
}
