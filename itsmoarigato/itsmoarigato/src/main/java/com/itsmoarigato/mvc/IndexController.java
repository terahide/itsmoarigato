package com.itsmoarigato.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for sending the user to the login view.
 *
 * @author Rob Winch
 *
 */
@Controller
public class IndexController {
	@RequestMapping("/")
	public String index() {
		return "main";
	}
	
	@RequestMapping("/my")
	public String my() {
		return "my";
	}

	@RequestMapping("/create")
	public String create() {
		return "create";
	}

	@RequestMapping("/update/{id}")
	public String update(@PathVariable("id")String id,Model model) {
		model.addAttribute("arigatoId", id);
		return "update";
	}
	
	@RequestMapping("/friend/{id}")
	public String friend(@PathVariable("id")String id,Model model){
		model.addAttribute("friendName",id);
		return "friend";
	}
}
