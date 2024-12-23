package mysite.controller;

import jakarta.servlet.ServletException;
import mysite.controller.action.main.MainAction;

public class MainServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		String config = getServletConfig().getInitParameter("config");
		System.out.println("MainController.init() called: " + config);
		super.init();
	}

	@Override
	protected Action getAction(String actionName) {
		return new MainAction();
	}
}