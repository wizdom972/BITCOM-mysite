package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("authUser") == null) {
			return;
		}
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		String author = authUser.getName();
		
		BoardVo vo = new BoardVo();
		
		vo.setTitle(title);
		vo.setContent(content);
		vo.setAuthor(author);
		vo.setGroup_no(0L);
		vo.setOrder_no(1L);
		vo.setDepth(0L);
		
		new BoardDao().insert(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
