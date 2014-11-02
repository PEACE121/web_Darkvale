package com.ntnu.game;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ntnu.game.model.gameLogic.GameLogic;
import com.ntnu.ui.JSP;


@SuppressWarnings("serial")
public class GameResetServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		GameLogic.reset();
		
		resp.sendRedirect(JSP.MAIN);
	}
}
