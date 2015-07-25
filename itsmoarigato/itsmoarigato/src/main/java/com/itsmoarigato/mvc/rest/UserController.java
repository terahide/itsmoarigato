package com.itsmoarigato.mvc.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itsmoarigato.User;
import com.itsmoarigato.model.UserManager;

@RestController
public class UserController {
	
	@Autowired
	UserManager userManager;
	
	@RequestMapping(value="/rest/user/{email}/image/{id}",method=RequestMethod.GET)
	ByteArrayResource image(@PathVariable("email")String email,@PathVariable("id")String id,HttpServletResponse res) {
		User user = userManager.getUser(email);
		
		return new ByteArrayResource(user.getImage().getContents());
	}
}
