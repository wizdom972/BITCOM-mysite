package mysite.controller.action.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.controller.ActionServlet.Action;
import mysite.dao.UserDao;
import mysite.vo.UserVo;

public class UpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Access Control
		HttpSession session = request.getSession();

		if (session == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}


		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String gender = request.getParameter("gender");


		UserVo updatedUser = new UserVo();
		updatedUser.setId(authUser.getId());
		updatedUser.setName(name);
		updatedUser.setEmail(authUser.getEmail()); // 이메일은 변경 불가
		updatedUser.setGender(gender);

		if (password != null && !password.isEmpty()) {
			updatedUser.setPassword(password);
		}


		UserDao userDao = new UserDao();
		int result = userDao.update(updatedUser);

		if (result > 0) {
			authUser.setName(name);
			authUser.setGender(gender);
			session.setAttribute("authUser", authUser);

			response.sendRedirect(request.getContextPath() + "/user?a=updateform");
		} else {
			//response.sendRedirect(request.getContextPath() + "/user?a=updateform");
		}
	}
}
