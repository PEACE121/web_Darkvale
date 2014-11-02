package com.ntnu.game.model.gameLogic.minigame;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ntnu.game.model.gameLogic.GameLogic;
import com.ntnu.game.model.gameLogic.finale.ETaskType;


@SuppressWarnings("serial")
public class MinigameActionsServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String type = checkNull(req.getParameter("type"));
		String value = checkNull(req.getParameter("user"));
		String redirectURL = checkNull(req.getParameter("redirect"));
		
		ETaskType taskType = ETaskType.valueOf(type);
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		GameLogic.getInstance().getFinalGame().getCurrentMinigame().responseOfServlet(user.getUserId(), taskType, value);
		
		resp.sendRedirect(redirectURL);
	}
	
	
	private String checkNull(String s)
	{
		if (s == null)
		{
			return "";
		}
		return s;
	}
}
