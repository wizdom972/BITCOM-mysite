package mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
    public MainController() {
        System.out.println("MainController initialized");
    }
	
	@RequestMapping({ "/", "/main", "/mysite04/" })
	public String main(Model model) {
		System.out.println("Handling request for: /mysite04/");
		return "main/index";
	}
}
