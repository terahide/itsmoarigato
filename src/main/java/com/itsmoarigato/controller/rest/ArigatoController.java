package com.itsmoarigato.controller.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsmoarigato.Message;
import com.itsmoarigato.Image;
import com.itsmoarigato.User;
import com.itsmoarigato.model.Arigato;
import com.itsmoarigato.model.Pagination;

@RestController
public class ArigatoController {
	
	@Autowired
	Arigato arigato;

	//TODO けす
	int id; 
	User fromUser; 
	User toUser;
	String subject;
	String contents; 
	Date created;
	List<Image> images;
	
	
    @RequestMapping(value="/rest/arigato",method=RequestMethod.GET)
    @ResponseBody
    List<Message> list(@RequestParam(value="type", required=false, defaultValue="arround") String type, Model model) {
    	List<Message> messages = new ArrayList<>();
    	messages.add(new Message(id, fromUser, toUser, subject, contents, created, images));
    	return messages;
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.GET)
    @ResponseBody
    Message detail(@PathVariable("id")String id,@RequestParam(value="type", required=false, defaultValue="around") String type, Model model) {//TODO aroundをenumに 
    	return new Message(toInt(id), fromUser, toUser, subject, contents, created, images);
    }
    
    @RequestMapping(value="/rest/arigato",method=RequestMethod.PUT)
    @ResponseBody
    String create(@Valid ArigatoCommand arigato,Model model) {
    	return "{\"sucsses\":true}";
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.POST)
    @ResponseBody
    String update(@Valid ArigatoCommand arigato, Model model) { 
    	return "{\"sucsses\":true}";
    }
    
    private static int toInt(String s){
    	if(isEmpty(s)){
    		return 0;
    	}
    	if( ! isNumber(s)){
    		return 0;
    	}
    	return Integer.parseInt(s);
    }

	private static boolean isNumber(String s) {
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	private static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
