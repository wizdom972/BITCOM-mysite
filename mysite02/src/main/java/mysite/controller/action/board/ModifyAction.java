package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));
		
		BoardVo vo = new BoardDao().findByNo(no);
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		vo.setTitle(title);
		vo.setContent(content);
		
		new BoardDao().update(vo);

		response.sendRedirect(request.getContextPath() + "/board?a=view&no=" + no);
	}

}
