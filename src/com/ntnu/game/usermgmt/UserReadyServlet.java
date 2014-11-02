package com.ntnu.game.usermgmt;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ntnu.game.model.UserDao;
import com.ntnu.game.model.UserEntity;
import com.ntnu.game.model.gameLogic.GameLogic;
import com.ntnu.ui.JSP;
import com.ntnu.ui.JSP;


@SuppressWarnings("serial")
public class UserReadyServlet extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		boolean ready = Boolean.parseBoolean(checkNull(req.getParameter("ready")));
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserEntity userEnt = UserDao.INSTANCE.getUser(user.getUserId()).get(0);
		GameLogic.getInstance().playerReady(userEnt);
		
		resp.sendRedirect(JSP.GAME);
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
