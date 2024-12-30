package mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mysite.security.Auth;
import mysite.security.AuthUser;
import mysite.service.BoardService;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	/**
	 * [ 목록 + 페이징 + 검색 ]
	 *   - 파라미터 p: 현재 페이지(디폴트=1)
	 *   - 파라미터 kwd: 검색어(디폴트=빈 문자열)
	 */
	@RequestMapping(value={"", "/", "/list"}, method=RequestMethod.GET)
	public String list(
			@RequestParam(value="p", required=false, defaultValue="1") int currentPage,
			@RequestParam(value="kwd", required=false, defaultValue="") String keyword,
			Model model) {
		
		// Service 호출 → 게시글 목록 + 페이징 정보 + 검색어 등
		Map<String, Object> map = boardService.getContentsList(currentPage, keyword);
		
		// JSP에서 사용할 수 있도록 Model에 담기
		model.addAttribute("map", map);
		
		// /WEB-INF/views/board/list.jsp 로 forward
		return "/board/list";
	}
	
	/**
	 * [ 글쓰기 폼 ]
	 *  - GET : 사용자가 "글쓰기" 버튼을 눌렀을 때, 폼 페이지로 이동
	 */
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String writeForm(Model model) {
		model.addAttribute("mode", "write");
		return "/board/write";
	}
	
	/**
	 * [ 글쓰기 처리 ]
	 *  - POST : 사용자가 폼에서 제목/내용을 입력 후 "등록" 버튼을 눌렀을 때
	 */
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(BoardVo boardVo, @AuthUser UserVo authUser) {
		
		System.out.println(boardVo);
		System.out.println(authUser);
		
		// 작성자 authUser의 이름으로 세팅
		boardVo.setAuthor(authUser.getName());
		
		// DB에 새 글 등록
		boardService.addContents(boardVo, authUser);
		// 목록 페이지로 리다이렉트
		return "redirect:/board";
	}
	
	/**
     * [ 답글 작성 폼 ]
     * - mode="reply" 로 세팅
     * - group_no, order_no, depth 를 파라미터로 전달받아 JSP에 hidden으로 넘김
     */
    @RequestMapping(value="/reply", method=RequestMethod.GET)
    public String replyForm(
        @RequestParam("group_no") Long groupNo,
        @RequestParam("order_no") Long orderNo,
        @RequestParam("depth")   Long depth,
        Model model) 
    {
        // JSP에서 Hidden으로 넘겨줄 Vo
        BoardVo replyInfo = new BoardVo();
        replyInfo.setGroup_no(groupNo);
        replyInfo.setOrder_no(orderNo);
        replyInfo.setDepth(depth);

        model.addAttribute("mode", "reply");
        model.addAttribute("board", replyInfo);  // "board"라는 이름으로 JSP에서 접근
        return "/board/write"; // 같은 JSP를 사용
    }

    /**
     * [ 답글 등록 처리 ]
     * - POST /board/reply
     */
    @Auth
    @RequestMapping(value="/reply", method=RequestMethod.POST)
    public String replyPost(BoardVo boardVo, @AuthUser UserVo authUser) {
        // 답글 추가 로직
    	boardVo.setAuthor(authUser.getName());
    	
    	boardVo.setOrder_no(boardVo.getOrder_no() + 1);
    	boardVo.setDepth(boardVo.getDepth() + 1);
    	
        boardService.addReply(boardVo);
        return "redirect:/board";
    }

	
	/**
	 * [ 글 보기 ]
	 *  - no(게시글 번호)를 받아서 조회
	 *  - 조회수 증가
	 */
	@RequestMapping(value="/view", method=RequestMethod.GET)
	public String view(
			@RequestParam("no") Long no, 
			Model model) {
		
		BoardVo boardVo = boardService.getContents(no);
		model.addAttribute("board", boardVo);
		
		return "/board/view";
	}
	
	/**
	 * [ 글 수정 폼 ]
	 *  - 특정 글을 불러와서, 제목/내용을 수정할 수 있는 화면 제공
	 */
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modifyForm(@RequestParam("no") Long no, Model model) {
		// 내 글인지 확인해야 하면 getContents(no, userNo)도 가능
		BoardVo boardVo = boardService.getContents(no);
		model.addAttribute("board", boardVo);
		
		return "/board/modify";
	}
	
	/**
	 * [ 글 수정 처리 ]
	 */
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(BoardVo boardVo) {
		boardService.updateContents(boardVo);
		return "redirect:/board";
	}
	
	/**
	 * [ 글 삭제 ]
	 *  - no + (userNo 가 필요하면 세션에서 가져와서 전달)
	 */
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(@RequestParam("no") Long no, @AuthUser UserVo authUser) {

		boardService.deleteContents(no, authUser.getName());
		
		return "redirect:/board";
	}
}
