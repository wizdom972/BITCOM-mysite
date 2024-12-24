package mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class SearchAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("kwd");
		if (keyword == null || keyword.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}

		BoardDao dao = new BoardDao();
		List<BoardVo> list = dao.search(keyword);

		request.setAttribute("boardList", list);
		request.setAttribute("keyword", keyword);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		rd.forward(request, response);
	}
}
