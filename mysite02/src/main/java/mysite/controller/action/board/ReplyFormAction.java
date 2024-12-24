package mysite.controller.action.board;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.vo.BoardVo;

public class ReplyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Long group_no = Long.parseLong(request.getParameter("group_no"));
		Long order_no = Long.parseLong(request.getParameter("order_no"));
		Long depth = Long.parseLong(request.getParameter("depth"));

		BoardVo vo = new BoardVo();
		vo.setGroup_no(group_no);
		vo.setOrder_no(order_no);
		vo.setDepth(depth);

		request.setAttribute("vo", vo);
		request.setAttribute("a", "replyForm");

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/write.jsp");
		rd.forward(request, response);
	}

}
