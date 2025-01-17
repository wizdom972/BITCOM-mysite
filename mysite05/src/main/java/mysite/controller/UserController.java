package mysite.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import mysite.service.UserService;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/join")
	public String join(@ModelAttribute UserVo userVo) {
		return "user/join";
	}

	@PostMapping("/join")
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel());

			return "user/join";
		}
		
		
		userService.join(userVo);

		return "redirect:/user/joinsuccess";
	}

	@RequestMapping("/joinsuccess")
	public String joinSuccess() {
		return "user/joinsuccess";
	}

	@RequestMapping(value="/login")
	public String login() {
		return "user/login";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(/* HttpSession session, */ Authentication authentication, Model model) {
		// 1. HttpSession을 사용하는 방법 <- 별로 좋은 방법은 아님
//		SecurityContext sc = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
//		Authentication authentication = sc.getAuthentication();
//		UserVo authUser = (UserVo) authentication.getPrincipal(); 
		
		// 2. SecurityContextHolder(Scpring Security ThreadLocal Helper Class)
//		SecurityContext sc = SecurityContextHolder.getContext();
//		Authentication authentication = sc.getAuthentication();
//		UserVo authUser = (UserVo) authentication.getPrincipal();
		
		// 3. parameter로 받아오기
		UserVo authUser = (UserVo) authentication.getPrincipal();	
		UserVo userVo = userService.getUser(authUser.getId());
		
		model.addAttribute("vo", userVo);
		return "user/update";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(Authentication authentication, UserVo userVo) {
		UserVo authUser = (UserVo) authentication.getPrincipal();	
		
		userVo.setId(authUser.getId());
		userService.update(userVo);
		
		authUser.setName(userVo.getName());
		return "redirect:/user/update";
	}
	
	@RequestMapping("/auth")
	public void auth() {
		
	}
	
	@RequestMapping("/logout")
	public void logout() {
		
	}
}
