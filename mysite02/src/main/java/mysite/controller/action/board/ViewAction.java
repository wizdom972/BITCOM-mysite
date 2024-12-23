package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Long no = Long.parseLong(request.getParameter("no"));
        
        BoardDao dao = new BoardDao();
        BoardVo board = dao.findByNo(no);
        dao.incrementHit(no);
        
        request.setAttribute("board", board);
        
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/view.jsp");
        rd.forward(request, response);

	}

}
