package com.ntnu.ui;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ntnu.game.model.UserDao;
import com.ntnu.game.model.UserEntity;
import com.ntnu.game.model.gameLogic.GameLogic;


public enum JSPCommonParts
{
	INSTANCE;
	
	public String getHeader(String webpage, String requestURI)
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		UserEntity userEntity = null;
		
		String url = userService.createLoginURL(requestURI);
		String urlLinktext = "Login";
		if (user != null)
		{
			url = userService.createLogoutURL(JSP.MAIN);
			urlLinktext = "Logout";
			List<UserEntity> userEntities = UserDao.INSTANCE.getUser(user.getUserId());
			if (userEntities.size() == 1)
			{
				userEntity = userEntities.get(0);
			} else
			{
				System.out.println("user not found");
			}
		}
		
		String header = "";
		header += "<div class='navbar navbar-inverse'>";
		header += " <div class='container'>";
		header += "  <div class='navbar-header'>";
		header += "   <button type='button' class='navbar-toggle collapsed' data-toggle='collapse' data-target='.navbar-collapse'>";
		header += "    <span class='sr-only'>Toggle navigation</span>";
		header += "    <span class='icon-bar'></span>";
		header += "    <span class='icon-bar'></span>";
		header += "    <span class='icon-bar'></span>";
		header += "   </button>";
		header += "   <a class='navbar-brand' href='" + JSP.MAIN + "'>Darkvale</a>";
		header += "  </div>";
		header += "  <div class='navbar-collapse collapse'>";
		header += "   <ul class='nav navbar-nav'>";
		header += "	   <li " + (webpage.equals(JSP.MAIN) ? "class='active'" : "") + "><a href='" + JSP.MAIN
				+ "'>Home</a></li>";
		header += (user != null ? "<li "
				+ (webpage.equals(JSP.GAME) || webpage.equals(JSP.PHASE) || webpage.equals(JSP.SLIDE) ? "class='active'"
						: "") + "><a href='" + JSP.GAME + "'>Game</a></li>" : "");
		header += "    <li class='right'><a href='" + url + "'>" + urlLinktext
				+ (user == null ? "" : " (" + user.getNickname() + ")") + "</a></li>";
		header += (user != null && userService.isUserAdmin() ? "    <li><a href='/gameReset'>Reset</a></li>" : "");
		header += (user != null && userService.isUserAdmin() && GameLogic.getInstance().getFinalGame() != null ? "    <li><a href='/nextRound'>Next Phase</a></li>"
				: "");
		header += "   </ul>";
		header += "   <ul class='nav navbar-nav navbar-right'>";
		header += (userEntity != null ? "    <li " + (webpage.equals(JSP.PROFILE) ? "class='active'" : "")
				+ "> <a href='" + JSP.PROFILE + "'>" + userEntity.getName() + "</a></li>" : "");
		header += "   </ul>";
		header += "   <ul class='nav navbar-nav navbar-right'>";
		header += (userEntity != null ? "    <li " + (webpage.equals(JSP.PROFILE) ? "class='active'" : "") + "><a href='"
				+ JSP.PROFILE + "'><img src='" + userEntity.getUserImage()
				+ "' class='img-responsive' style='height:20px;;'></a></li>" : "");
		header += "   </ul>";
		header += "  </div><!--/.nav-collapse -->";
		header += " </div>";
		header += "</div>";
		
		// redirect if the game has started already
		String redirectLogic = "";
		if (!webpage.equals(JSP.MAIN))
		{
			if (GameLogic.getInstance().isReady() || GameLogic.getInstance().isRunning())
			{
				redirectLogic = "<script type='text/javascript'>window.location = '" + JSP.MAIN
						+ "'; alert('Sorry. The Game has already started!')</script>";
				for (UserEntity player : GameLogic.getInstance().getActiveUsers())
				{
					if (player.getGoogleUserId().equals(user.getUserId()))
					{
						redirectLogic = "";
					}
				}
			}
			// redirect if the game has not started yet
			if (!GameLogic.getInstance().isReady() && !GameLogic.getInstance().isRunning()
					&& (webpage.equals(JSP.PHASE) || webpage.equals(JSP.SLIDE)))
			{
				redirectLogic = "<script type='text/javascript'>window.location = '" + JSP.MAIN + "';</script>";
			}
		}
		
		header += redirectLogic;
		
		
		return header;
	}
	
	
	public String getHead()
	{
		String head = "";
		head += "<head>";
		head += " <meta charset='utf-8'>";
		head += " <meta http-equiv='X-UA-Compatible' content='IE=edge'>";
		head += " <meta name='viewport' content='width=device-width, initial-scale=1'>";
		head += " <title>The Game</title>";
		
		head += "<link href='werewolf.css' rel='stylesheet'>";
		
		head += " <!-- Bootstrap -->";
		head += "	   <link href='css/bootstrap.css' rel='stylesheet'>";
		head += "	   <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->";
		head += "	   <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->";
		head += " 		<!--[if lt IE 9]>";
		head += "  			<script src='https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js'></script>";
		head += "  			<script src='https://oss.maxcdn.com/respond/1.4.2/respond.min.js'></script>";
		head += " 		<![endif]-->";
		head += "</head>";
		
		return head;
	}
}
