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
	private final static String me = "takashi@hoge.co.jp";
	
    @RequestMapping(value="/rest/arigato",method=RequestMethod.GET)
    @ResponseBody
    List<Message> list(@RequestParam(value="type", required=false, defaultValue="around") String type, Model model) {
    	//TODO ページネーションどうしようね。
    	List<Message> messages;
    	if(type.equals(GetType.mine.name())){
    		messages = arigato.getMineMessages(me, new Pagination());
    	}else{
    		messages = arigato.getAroundMessages(me, new Pagination());
    	}
    	return messages;
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.GET)
    @ResponseBody
    Message detail(@PathVariable("id")String id, Model model) {
    	return arigato.getMessage(toInt(id));
    }
    
    @RequestMapping(value="/rest/arigato",method=RequestMethod.PUT)
    @ResponseBody
    String create(@Valid ArigatoCommand arigato,Model model) {
    	this.arigato.add(toMessage(arigato));
    	return "{\"sucsses\":true}";
    }

    private Message toMessage(ArigatoCommand arigato) {
    	Message message = new Message(arigato.getId(),toUser(arigato.fromUserId),toUser(arigato.toUserId),arigato.subject,arigato.message,null,new ArrayList<>());
		return message;
	}

	private User toUser(String userId) {
		User user = new User(userId,null);
		return user;
	}

	@RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.POST)
    @ResponseBody
    String update(@Valid ArigatoCommand arigato, Model model) { 
    	this.arigato.update(arigato.getId(), arigato.getSubject(), arigato.getMessage());
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
