package com.ntnu.game.model.gameLogic.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ntnu.game.model.gameLogic.IMinigame;
import com.ntnu.game.model.gameLogic.finale.EFinaleState;
import com.ntnu.game.model.gameLogic.finale.ETaskType;
import com.ntnu.game.model.gameLogic.finale.FinaleLogic;
import com.ntnu.game.model.gameLogic.finale.Task;
import com.ntnu.ui.JSP;


public abstract class AAngryCitizenMinigame implements IMinigame
{
	protected final FinaleLogic			finale;
	
	protected Map<String, ACRole>			acRoles;
	
	private final Map<String, String>	chosenPlayers	= new HashMap<String, String>();
	
	private final Map<String, Long>		buttonClicked	= new HashMap<String, Long>();
	
	private final Map<String, String>	input				= new HashMap<String, String>();
	
	
	/**
	 * @param finale
	 */
	public AAngryCitizenMinigame(FinaleLogic finale)
	{
		super();
		this.finale = finale;
	}
	
	
	public List<Task> getTasks(String userId)
	{
		if (finale.getCurrentState() != EFinaleState.DAY || !acRoles.containsKey(userId))
		{
			return new ArrayList<Task>();
		}
		return acRoles.get(userId).getTasks();
	}
	
	
	@Override
	public String getCustomGameHTMLCode(String userId)
	{
		if (finale.getCurrentState() != EFinaleState.DAY)
		{
			return "";
		} else if (!acRoles.containsKey(userId))
		{
			return "<div style='margin: auto; max-width: 100px;'>You do not play in this round</div>";
		} else
		{
			String response = "";
			for (Task task : getTasks(userId))
			{
				String action = task.getRedirectURL();
				switch (task.getTaskType())
				{
					case USER_SELECTION:
						String chosenPlayer = "";
						if (chosenPlayers.containsKey(userId))
						{
							chosenPlayer = chosenPlayers.get(userId);
						}
						response += finale.getUserSelection(userId, task.getTaskText(), action, chosenPlayer, JSP.PHASE,
								false);
						response += "<br>";
						break;
					case BUTTON:
						response += "<div class='row'><div class='col-md-12'><div style='text-align:center;'><h1>"
								+ task.getTaskText() + "</h1></div></div></div>\n";
						
						response += "<div style='margin: auto; max-width: 100px;'>";
						response += "<div class='row'><div class='col-md-12'>";
						response += " <form name='input' action='/" + action + "' method='get'>";
						response += "  <input type='hidden' name='user' value='" + userId + "' /> ";
						response += "  <input type='hidden' name='type' value='" + task.getTaskType().name() + "' /> ";
						response += "  <input type='hidden' name='redirect' value='" + JSP.PHASE + "' /> ";
						response += "  <button " + (buttonClicked.containsKey(userId) ? "disabled" : "")
								+ " class='btn btn-default' style='width:100%' type='submit' > Meet now </button>";
						response += " </form>";
						response += "</div></div>";
						response += "</div>";
						
						break;
					case INPUT_FIELD:
						response += "<div class='row'><div class='col-md-12'><div style='text-align:center;'><h1>"
								+ task.getTaskText() + "</h1></div></div></div>\n";
						
						response += "<div style='margin: auto; max-width: 200px;'>";
						response += "<div class='row'><div class='col-md-12'>";
						response += " <form name='input' action='/" + action + "' method='get'>";
						response += "  <input type='hidden' name='type' value='" + task.getTaskType().name() + "' /> ";
						response += "  <input type='hidden' name='redirect' value='" + JSP.PHASE + "' /> ";
						String value = "";
						if (input.containsKey(userId))
						{
							value = input.get(userId);
						}
						response += "  <input class='form-control' type='text' name='user' value='" + value + "'></input>";
						response += " </form>";
						response += "</div></div>";
						response += "</div>";
				}
			}
			return response;
		}
	}
	
	
	@Override
	public String responseOfServlet(String userId, ETaskType taskType, String value)
	{
		switch (taskType)
		{
			case USER_SELECTION:
				chosenPlayers.put(userId, value);
				break;
			case INPUT_FIELD:
				input.put(userId, value);
				break;
			case BUTTON:
				buttonClicked.put(userId, System.currentTimeMillis());
			default:
				break;
		}
		return null;
	}
	
	
	@Override
	public Map<String, Integer> evaluate()
	{
		Map<String, Integer> points = new HashMap<String, Integer>();
		for (Entry<String, ACRole> role : acRoles.entrySet())
		{
			System.out.println(role.getKey());
			int pointsTemp = 0;
			for (Task task : role.getValue().getTasks())
			{
				switch (task.getTaskType())
				{
					case BUTTON:
						if (buttonClicked.containsKey(role.getKey())
								&& buttonClicked.containsKey(task.getRelatedPlayers().get(0)))
						{
							System.out.println("Both clicked button");
							pointsTemp++;
							long timeBetween = Math.abs(buttonClicked.get(role.getKey())
									- buttonClicked.get(task.getRelatedPlayers().get(0)));
							if (timeBetween < 1000)
							{
								System.out.println("fewer than 1000ms");
								pointsTemp++;
							}
							if (timeBetween < 10000)
							{
								System.out.println("fewer than 10000ms");
								pointsTemp++;
							}
						}
						break;
					case INPUT_FIELD:
						if (input.containsKey(role.getKey()))
						{
							if (input.get(role.getKey()).toLowerCase().contains(task.getCorrectAnswer().toLowerCase()))
							{
								System.out.println("Input field correct");
								pointsTemp += 3;
							}
						}
						break;
					case USER_SELECTION:
						if (chosenPlayers.containsKey(role.getKey()))
						{
							if (chosenPlayers.get(role.getKey()).equals(task.getCorrectAnswer()))
							{
								System.out.println("User selection correct");
								pointsTemp += 3;
							}
						}
						break;
				}
			}
			points.put(role.getKey(), pointsTemp / (role.getValue().getTasks().size()));
		}
		return points;
	}
	
	
	@Override
	public abstract List<String> getGeneralDescription();
	
	
	@Override
	public void reset()
	{
		chosenPlayers.clear();
		input.clear();
		buttonClicked.clear();
	}
	
	
	@Override
	public List<String> getSpecialInformation(String userId)
	{
		if (acRoles.containsKey(userId))
		{
			return acRoles.get(userId).getSpecialInformation();
		}
		return new ArrayList<String>();
	}
}
