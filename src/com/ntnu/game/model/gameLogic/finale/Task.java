package com.ntnu.game.model.gameLogic.finale;

import java.util.ArrayList;
import java.util.List;


public class Task
{
	private final String			taskText;
	private final String			redirectURL;
	private final ETaskType		taskType;
	private final List<String>	relatedPlayers	= new ArrayList<String>();
	private String					correctAnswer	= "";
	
	
	/**
	 * @param taskText
	 * @param redirectURL
	 */
	public Task(String taskText, String redirectURL, ETaskType taskType)
	{
		super();
		this.taskText = taskText;
		this.redirectURL = redirectURL;
		this.taskType = taskType;
	}
	
	
	public void addRelatedPlayer(String userId)
	{
		relatedPlayers.add(userId);
	}
	
	
	/**
	 * @return the correctAnswer
	 */
	public String getCorrectAnswer()
	{
		return correctAnswer;
	}
	
	
	/**
	 * @param correctAnswer the correctAnswer to set
	 */
	public void setCorrectAnswer(String correctAnswer)
	{
		this.correctAnswer = correctAnswer;
	}
	
	
	/**
	 * @return the relatedPlayers
	 */
	public List<String> getRelatedPlayers()
	{
		return relatedPlayers;
	}
	
	
	/**
	 * @return the taskText
	 */
	public String getTaskText()
	{
		return taskText;
	}
	
	
	/**
	 * @return the redirectURL
	 */
	public String getRedirectURL()
	{
		return redirectURL;
	}
	
	
	/**
	 * @return the taskType
	 */
	public ETaskType getTaskType()
	{
		return taskType;
	}
	
	
}
