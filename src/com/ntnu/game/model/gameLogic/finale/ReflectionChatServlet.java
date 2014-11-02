package com.ntnu.game.model.gameLogic.finale;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ntnu.game.model.gameLogic.GameLogic;


@SuppressWarnings("serial")
public class ReflectionChatServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.getWriter().write(GameLogic.getInstance().getFinalGame().getReflectionChatText());
	}
	
}
