package com.itsmoarigato.mvc.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.itsmoarigato.Image;
import com.itsmoarigato.Message;
import com.itsmoarigato.User;
import com.itsmoarigato.model.Arigato;
import com.itsmoarigato.model.Pagination;
import com.itsmoarigato.model.exception.NotFoundException;

@RestController
public class ArigatoController {
	
	@Autowired
	Arigato arigato;

    private String me(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    
	@RequestMapping(value="/rest/arigato",method=RequestMethod.GET)
    @ResponseBody
    List<Message> list(@RequestParam(value="type", required=false, defaultValue="around") String type) {
    	//TODO ページネーションどうしようね。
    	List<Message> messages;
    	if(type.equals(GetType.mine.name())){
    		messages = arigato.getMineMessages(me(), new Pagination());
    	}else if(type.equals(GetType.wrote.name())){
    		messages = arigato.getWrittenMessages(me(), new Pagination());
    	}else{
    		messages = arigato.getAroundMessages(me(), new Pagination());
    	}
    	return messages;
    }

    @RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.GET)
    @ResponseBody
    Message detail(@PathVariable("id")String id) {
    	Message message = arigato.getMessage(toInt(id));
    	if(message == null){
    		throw new NotFoundException();
    	}
    	return message;
    }
    
    @RequestMapping(value="/rest/arigato",method=RequestMethod.POST)
    @ResponseBody
    Json add(@Valid ArigatoCommand arigato,Principal principal) {
    	int arigatoId = this.arigato.add(toMessage(arigato,principal.getName()));
    	return new Json("{\"success\":true,\"arigatoId\":" + arigatoId + "}");
    }

    @RequestMapping(value="/rest/arigato/{arigatoId}/image",method=RequestMethod.POST)
    @ResponseBody
    String add(@Valid FileUploadCommand uploaded,Principal principal) throws IOException {
    	
    	File f = File.createTempFile("~~~", "~~~");
    	try(OutputStream out =  new FileOutputStream(f)){
    		IOUtils.copy(uploaded.file.getInputStream(), out);
    	}
    	return "{\"success\":true}";
    }

    private Message toMessage(ArigatoCommand arigato,String fromUserId) {
    	Message message = new Message(toInt(arigato.getId()),toUser(fromUserId),toUser(arigato.toUserId),arigato.subject,arigato.message,null,new ArrayList<Image>());
		return message;
	}

	private User toUser(String userId) {
		User user = new User(userId,null);
		return user;
	}

	@RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.POST)
    @ResponseBody
    Json update(@Valid ArigatoCommand arigato) {
		{
			Message message = this.arigato.getMessage(toInt(arigato.getId()));
	    	if(message == null){
	    		throw new NotFoundException();
	    	}
		}
    	this.arigato.update(toInt(arigato.getId()), arigato.getSubject(), arigato.getMessage());
    	return new Json("{\"success\":true}");
    }
    
	@RequestMapping(value="/rest/arigato/{id}",method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
    Json delete(@PathVariable("id")String id) { 
    	this.arigato.delete(toInt(id));
    	return new Json("{\"success\":true}");
    }
    
    private static int toInt(String s){
    	if(isEmpty(s)){
    		return 0;
    	}
		try{
			return Integer.parseInt(s);
		}catch(NumberFormatException e){
			throw new NotFoundException(e);
		}
	}

	private static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	class Json {

	    private final String value;

	    public Json(String value) {
	        this.value = value;
	    }

	    @JsonValue
	    @JsonRawValue
	    public String value() {
	        return value;
	    }
	}
}
