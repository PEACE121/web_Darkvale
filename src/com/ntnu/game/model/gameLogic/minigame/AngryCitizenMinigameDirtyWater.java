package com.ntnu.game.model.gameLogic.minigame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ntnu.game.model.UserEntity;
import com.ntnu.game.model.gameLogic.finale.ETaskType;
import com.ntnu.game.model.gameLogic.finale.FinaleLogic;
import com.ntnu.game.model.gameLogic.finale.Task;


public class AngryCitizenMinigameDirtyWater extends AAngryCitizenMinigame
{
	
	
	public AngryCitizenMinigameDirtyWater(FinaleLogic finale)
	{
		super(finale);
		
		acRoles = generateAngryCitizenRoles(finale.getPlayers());
	}
	
	
	public Map<String, ACRole> generateAngryCitizenRoles(Map<String, UserEntity> allPlayers)
	{
		Map<String, ACRole> roles = new HashMap<String, ACRole>();
		
		List<String> possiblePlayers = new ArrayList<String>(allPlayers.keySet());
		Collections.shuffle(possiblePlayers);
		
		// 0
		List<Task> tasks0 = new ArrayList<Task>();
		Task task1 = new Task("Meet with " + finale.getPlayers().get(possiblePlayers.get(1)).getName()
				+ "! Click on this button at the same time.", "minigame", ETaskType.BUTTON);
		task1.addRelatedPlayer(possiblePlayers.get(1));
		tasks0.add(task1);
		Task task2 = new Task("Ask " + finale.getPlayers().get(possiblePlayers.get(2)).getName()
				+ " how to get clean water?", "minigame", ETaskType.INPUT_FIELD);
		task2.setCorrectAnswer("well");
		task2.addRelatedPlayer(possiblePlayers.get(2));
		tasks0.add(task2);
		List<String> specialInformation0 = new ArrayList<String>();
		specialInformation0.add("It is all the fault of the Mayor!");
		roles.put(possiblePlayers.get(0), new ACRole("Angry Citizen",
				"You are angry because the water of the village is dirty!", tasks0, specialInformation0));
		
		// 1
		List<Task> tasks1 = new ArrayList<Task>();
		task1 = new Task("Meet with " + finale.getPlayers().get(possiblePlayers.get(0)).getName()
				+ "! Click on this button at the same time.", "minigame", ETaskType.BUTTON);
		task1.addRelatedPlayer(possiblePlayers.get(0));
		tasks1.add(task1);
		task2 = new Task("Ask " + finale.getPlayers().get(possiblePlayers.get(2)).getName() + " who could help?",
				"minigame", ETaskType.USER_SELECTION);
		task2.setCorrectAnswer(possiblePlayers.get(1));
		task2.addRelatedPlayer(possiblePlayers.get(2));
		tasks1.add(task2);
		List<String> specialInformation1 = new ArrayList<String>();
		specialInformation1.add("It is all the fault of the Mayor!");
		roles.put(possiblePlayers.get(1), new ACRole("Angry Citizen",
				"You are angry because the water of the village is dirty!", tasks1, specialInformation1));
		
		// 2
		List<Task> tasks2 = new ArrayList<Task>();
		task1 = new Task("Ask " + finale.getPlayers().get(possiblePlayers.get(0)).getName() + " whose fault it is?",
				"minigame", ETaskType.INPUT_FIELD);
		task1.addRelatedPlayer(possiblePlayers.get(0));
		task1.setCorrectAnswer("Mayor");
		tasks2.add(task1);
		List<String> specialInformation2 = new ArrayList<String>();
		specialInformation2.add("A new well would solve the problem and perhaps "
				+ finale.getPlayers().get(possiblePlayers.get(0)).getName() + " could help.");
		roles.put(possiblePlayers.get(2), new ACRole("Angry Citizen",
				"You are angry because the water of the village is dirty!", tasks2, specialInformation2));
		
		return roles;
	}
	
	
	@Override
	public List<String> getGeneralDescription()
	{
		List<String> generalDescription = new ArrayList<String>();
		generalDescription.add("The water in the village is dirty. The citizens are angry!!!");
		return generalDescription;
	}
	
	
	@Override
	public void reset()
	{
		super.reset();
		acRoles = generateAngryCitizenRoles(finale.getPlayers());
	}
}
