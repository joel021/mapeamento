package com.jis.community.map.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
//@RequestMapping("/file_upload")
public class FileUpload {

	@Autowired
	ServletContext context;
	
	@RequestMapping(value="/file_upload", method = RequestMethod.POST)
	public @ResponseBody  Map<String, Object>  fileUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<String> list = new ArrayList<String>();
		
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		while(itr.hasNext()) {
			mpf = request.getFile(itr.next());
			
			try {
				File diretorio = new File(context.getRealPath("/resources"));
				
				if(!diretorio.exists())
					diretorio.mkdir();
	            
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(context.getRealPath("/resources")+"/"+mpf.getOriginalFilename().replace(" ", "-")));
				list.add(context.getRealPath("/resources")+"/"+mpf.getOriginalFilename().replace(" ", "-"));
				map.put("Status", 200);
			}catch(IOException ex){
				System.out.println("\n\n\n o erro é "+ex.toString());
				map.put("Status", 500);
				ex.printStackTrace();
			}
		}
		
		
		map.put("SucessfulList", list);		
	
		return map;
	}
	
}
