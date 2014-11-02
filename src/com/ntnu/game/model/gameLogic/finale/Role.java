package com.ntnu.game.model.gameLogic.finale;


public class Role
{
	private final String		name;
	private final ERoleType	role;
	
	private final boolean	villager;
	
	
	/**
	 * @param name
	 * @param role
	 */
	public Role(String name, ERoleType role, boolean villager)
	{
		super();
		this.name = name;
		this.role = role;
		this.villager = villager;
	}
	
	
	/**
	 * @return the role
	 */
	public ERoleType getRole()
	{
		return role;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @return the villager
	 */
	public boolean isVillager()
	{
		return villager;
	}
	
	
}
