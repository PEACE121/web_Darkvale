package com.ntnu.game.model.gameLogic;

import java.util.List;
import java.util.Map;

import com.ntnu.game.model.gameLogic.finale.ETaskType;


public interface IMinigame
{
	public String responseOfServlet(String userId, ETaskType taskType, String value);
	
	
	public String getCustomGameHTMLCode(String userId);
	
	
	public Map<String, Integer> evaluate();
	
	
	public List<String> getGeneralDescription();
	
	
	public List<String> getSpecialInformation(String userId);
	
	
	public void reset();
	
	
}
