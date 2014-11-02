package com.ntnu.game.model.gameLogic.finale;

public class TRole
{
	private final String			name;
	private final ETRoleType	role;
	
	
	/**
	 * @param name
	 * @param role
	 */
	public TRole(String name, ETRoleType role)
	{
		super();
		this.name = name;
		this.role = role;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @return the role
	 */
	public ETRoleType getRole()
	{
		return role;
	}
	
	
}
