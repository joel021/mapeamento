package com.jis.community.map.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {


  @RequestMapping("/error")
  @ResponseBody
  public String handleError(HttpServletRequest request) {
      
      return "/error";
  }

  @Override
  public String getErrorPath() {
      return "/error";
  }
  
}
