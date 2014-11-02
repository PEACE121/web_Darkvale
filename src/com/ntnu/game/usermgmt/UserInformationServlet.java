package com.ntnu.game.usermgmt;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ntnu.game.model.UserDao;
import com.ntnu.ui.JSP;


@SuppressWarnings("serial")
public class UserInformationServlet extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String profileImage = checkNull(req.getParameter("profileImage"));
		String username = checkNull(req.getParameter("username"));
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserDao.INSTANCE.updateProfileInformation(user.getUserId(), username, profileImage);
		
		resp.sendRedirect(JSP.MAIN);
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
