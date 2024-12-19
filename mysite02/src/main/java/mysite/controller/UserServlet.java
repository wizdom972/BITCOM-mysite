package mysite.controller;

import java.util.Map;

import jakarta.servlet.annotation.WebServlet;
import mysite.controller.action.main.MainAction;
import mysite.controller.action.user.JoinAction;
import mysite.controller.action.user.JoinFormAction;
import mysite.controller.action.user.JoinSuccessAction;
import mysite.controller.action.user.LoginAction;
import mysite.controller.action.user.LoginFormAction;
import mysite.controller.action.user.LogoutAction;
import mysite.controller.action.user.UpdateAction;
import mysite.controller.action.user.UpdateFormAction;

@WebServlet("/user")
public class UserServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Action> mapAction = Map.of(
			"joinform", new JoinFormAction(),
			"join", new JoinAction(),
			"joinsuccess", new JoinSuccessAction(),
			"loginform", new LoginFormAction(), 
			"login", new LoginAction(),
			"logout", new LogoutAction(),
			"updateform", new UpdateFormAction(),
			"update", new UpdateAction()
			);

	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new MainAction());
	}
}