package mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mysite.service.GuestbookService;
import mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

	@Autowired
	private GuestbookService guestbookService;
	
	
	@RequestMapping(value="", method= RequestMethod.GET)
	public String list(Model model) {
		List<GuestbookVo> list = guestbookService.getContentsList(); 
		
		model.addAttribute("list", list);
		
		return "/guestbook/list";
	}
	
	@RequestMapping(value="", method= RequestMethod.POST)
	public String list(GuestbookVo guestbookVo) {
		
		guestbookService.addContents(guestbookVo);
		
		return "redirect:/guestbook";
	}
	
	@RequestMapping(value="/delete", method= RequestMethod.GET)
	public String delete(@RequestParam("id") Long id, Model model) {
		
		model.addAttribute("id", id);
		
		return "guestbook/delete";
	}
	
	@RequestMapping(value="/delete", method= RequestMethod.POST)
	public String delete(@RequestParam("id") Long id, @RequestParam("password") String password, Model model) {
		
		guestbookService.deleteContents(id, password);
        
		return "redirect:/guestbook";
	}
}
