package com.ntnu.game.model.gameLogic.finale;

import java.util.ArrayList;
import java.util.List;


public class SocialMediaType
{
	private final String							name;
	private final List<EReflectionTypes>	reflectionTypes;
	
	
	/**
	 * @param name
	 * @param reflectionTypes
	 */
	public SocialMediaType(String name)
	{
		super();
		this.name = name;
		this.reflectionTypes = new ArrayList<EReflectionTypes>();
		reflectionTypes.add(EReflectionTypes.WORD_CLOUD);
	}
	
	
	/**
	 * @param name
	 * @param reflectionTypes
	 */
	public SocialMediaType(String name, List<EReflectionTypes> reflectionTypes)
	{
		super();
		this.name = name;
		this.reflectionTypes = reflectionTypes;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @return the reflectionTypes
	 */
	public List<EReflectionTypes> getReflectionTypes()
	{
		return reflectionTypes;
	}
	
	
}
