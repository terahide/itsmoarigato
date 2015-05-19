package com.itsmoarigato.controller.rest;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsmoarigato.Arigato;
import com.itsmoarigato.User;

@RestController
public class ArigatoController {

	int id; 
	int historyId; 
	User fromUser; 
	User toUser;
	String subject;
	String message; 
	List images;
	
    @RequestMapping("/rest/arigato")
    @ResponseBody
    Arigato home(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    	return new Arigato(id, historyId, fromUser, toUser, subject, message, images);
    }
}
