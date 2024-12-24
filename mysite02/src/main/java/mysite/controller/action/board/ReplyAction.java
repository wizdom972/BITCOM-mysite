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

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("authUser") == null) {
			response.sendRedirect(request.getContextPath() + "/user?a=login");
			return;
		}

		String author = ((UserVo) session.getAttribute("authUser")).getName();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Long group_no = Long.parseLong(request.getParameter("group_no"));
		Long order_no = Long.parseLong(request.getParameter("order_no")) + 1;
		Long depth = Long.parseLong(request.getParameter("depth")) + 1;

		BoardVo vo = new BoardVo();
		
		vo.setTitle(title);
		vo.setContent(content);
		vo.setAuthor(author);
		vo.setGroup_no(group_no);
		vo.setOrder_no(order_no);
		vo.setDepth(depth);

		new BoardDao().reply(vo);
		
		response.sendRedirect(request.getContextPath() + "/board");
	}

}
