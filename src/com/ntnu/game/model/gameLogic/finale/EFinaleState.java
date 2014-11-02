package com.ntnu.game.model.gameLogic.finale;

public enum EFinaleState
{
	INTRO("Night", 0),
	DAY("Day", 1800000),
	NIGHT("Night", 600000),
	REFLECTION("Reflection", 600000),
	LAST_REFLECTION("Reflection", 1000000);
	
	String	name;
	long		ms;
	
	
	EFinaleState(String name, long ms)
	{
		this.name = name;
		this.ms = ms;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @return the ms
	 */
	public long getMs()
	{
		return ms;
	}
	
	
}
