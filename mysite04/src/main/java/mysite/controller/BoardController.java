package mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mysite.security.Auth;
import mysite.security.AuthUser;
import mysite.service.BoardService;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;
import mysite.web.util.WebUtil;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String index(
		@RequestParam(value="p", required=true, defaultValue="1") Integer page,
		@RequestParam(value="kwd", required=true, defaultValue="") String keyword,
		Model model) {
		
		Map<String, Object> map = boardService.getContentsList(page, keyword);

		// model.addAllAttributes(map);
		model.addAttribute("map", map);
		model.addAttribute("keyword", keyword);
		
		return "board/index";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		BoardVo boardVo = boardService.getContents(no);
		model.addAttribute("boardVo", boardVo);
		return "board/view";
	}
	
	@Auth
	@RequestMapping("/delete/{no}")
	public String delete(
		@AuthUser UserVo authUser,
		@PathVariable("no") Long boardNo,
		@RequestParam(value="p", required=true, defaultValue="1") Integer page,
		@RequestParam(value="kwd", required=true, defaultValue="") String keyword) {		
		boardService.deleteContents(boardNo, authUser.getId());
		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}
	
	@Auth
	@RequestMapping("/modify/{no}")	
	public String modify(@AuthUser UserVo authUser, @PathVariable("no") Long no, Model model) {
		BoardVo boardVo = boardService.getContents(no, authUser.getId());
		model.addAttribute("boardVo", boardVo);
		return "board/modify";
	}

	@Auth
	@RequestMapping(value="/modify", method=RequestMethod.POST)	
	public String modify(
		@AuthUser UserVo authUser, 
		BoardVo boardVo,
		@RequestParam(value="p", required=true, defaultValue="1") Integer page,
		@RequestParam(value="kwd", required=true, defaultValue="") String keyword) {		
		boardVo.setUserId(authUser.getId());
		boardService.modifyContents(boardVo);
		return "redirect:/board/view/" + boardVo.getNo() + 
				"?p=" + page + 
				"&kwd=" + WebUtil.encodeURL( keyword, "UTF-8" );
	}

	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)	
	public String write() {
		return "board/write";
	}

	@Auth
	@RequestMapping(value="/write", method=RequestMethod.POST)	
	public String write(
		@AuthUser UserVo authUser,
		@ModelAttribute BoardVo boardVo,
		@RequestParam(value="p", required=true, defaultValue="1") Integer page,
		@RequestParam(value="kwd", required=true, defaultValue="") String keyword) {

		boardVo.setUserId(authUser.getId());
		boardService.addContents(boardVo);
		return	"redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(keyword, "UTF-8");
	}

	@Auth
	@RequestMapping(value="/reply/{no}")	
	public String reply(@PathVariable("no") Long no, Model model) {
		BoardVo boardVo = boardService.getContents(no);
		boardVo.setOrderNo(boardVo.getOrderNo() + 1);
		boardVo.setDepth(boardVo.getDepth() + 1);
		
		model.addAttribute("boardVo", boardVo);
		
		return "board/reply";
	}	
}