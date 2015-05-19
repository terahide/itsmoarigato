package com.itsmoarigato.controller.rest;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsmoarigato.Arigato;
import com.itsmoarigato.Image;
import com.itsmoarigato.User;

@RestController
public class ArigatoController {
	
	//TODO モデリング

	//TODO けす
	int id; 
	int historyId; 
	User fromUser; 
	User toUser;
	String subject;
	String message; 
	List<Image> images;
	
    @RequestMapping(value="/rest/arigato",method=RequestMethod.GET)
    @ResponseBody
    Arigato list(@RequestParam(value="type", required=false, defaultValue="around") String type, Model model) {//TODO aroundをenumに 
    	return new Arigato(id, historyId, fromUser, toUser, subject, message, images);
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.GET)
    @ResponseBody
    Arigato detail(@PathVariable("id")String id,@RequestParam(value="type", required=false, defaultValue="around") String type, Model model) {//TODO aroundをenumに 
    	return new Arigato(toInt(id), historyId, fromUser, toUser, subject, message, images);
    }
    
    @RequestMapping(value="/rest/arigato",method=RequestMethod.PUT)
    @ResponseBody
    String create(Model model) {//TODO パラメータたち
    	return "{\"sucsses\":true}";
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.POST)
    @ResponseBody
    String update(@PathVariable("id")String id, Model model) {//TODO パラメータたち 
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
