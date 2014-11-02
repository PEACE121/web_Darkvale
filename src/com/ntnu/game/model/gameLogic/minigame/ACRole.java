package com.ntnu.game.model.gameLogic.minigame;

import java.util.List;

import com.ntnu.game.model.gameLogic.finale.Task;


public class ACRole
{
	private final String			name;
	private final String			taskText;
	private final List<Task>	tasks;
	private final List<String>	specialInformation;
	
	
	/**
	 * @param name
	 * @param taskText
	 * @param tasks
	 */
	public ACRole(String name, String taskText, List<Task> tasks, List<String> specialInformation)
	{
		super();
		this.name = name;
		this.taskText = taskText;
		this.tasks = tasks;
		this.specialInformation = specialInformation;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @return the taskText
	 */
	public String getTaskText()
	{
		return taskText;
	}
	
	
	/**
	 * @return the tasks
	 */
	public List<Task> getTasks()
	{
		return tasks;
	}
	
	
	/**
	 * @return the specialInformation
	 */
	public List<String> getSpecialInformation()
	{
		return specialInformation;
	}
	
	
}
