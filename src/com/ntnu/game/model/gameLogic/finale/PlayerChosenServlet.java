package com.ntnu.game.model.gameLogic.finale;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ntnu.game.model.gameLogic.GameLogic;


@SuppressWarnings("serial")
public class PlayerChosenServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String userChosen = checkNull(req.getParameter("user"));
		String redirectURL = checkNull(req.getParameter("redirect"));
		String doubleLynch = checkNull(req.getParameter("doubleLynch"));
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		GameLogic.getInstance().getFinalGame().playerChosen(user.getUserId(), userChosen, doubleLynch);
		
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
