package com.ntnu.game.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class UserEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long		id;
	private String		googleUserId;
	private String		name;
	private String		password;
	private String		userImage	= "images/dummyProfileImage.jpg";
	
	@Transient
	private boolean	ready			= false;
	
	@Transient
	private boolean	lynched		= false;
	
	@Transient
	private boolean	hanged		= false;
	
	
	public UserEntity(String googleUserId, String name, String password)
	{
		this.googleUserId = googleUserId;
		this.name = name;
		this.password = password;
	}
	
	
	/**
	 * @param googleUserId the googleUserId to set
	 */
	public void setGoogleUserId(String googleUserId)
	{
		this.googleUserId = googleUserId;
	}
	
	
	/**
	 * @return the googleUserId
	 */
	public String getGoogleUserId()
	{
		return googleUserId;
	}
	
	
	@Deprecated
	public Long getId()
	{
		return id;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}
	
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	
	/**
	 * @return the ready
	 */
	public boolean isReady()
	{
		return ready;
	}
	
	
	/**
	 * @param ready the ready to set
	 */
	public void setReady(boolean ready)
	{
		this.ready = ready;
	}
	
	
	/**
	 * @return the userImage
	 */
	public String getUserImage()
	{
		if (userImage == null)
		{
			return "images/dummyProfileImage.jpg";
		}
		return userImage;
	}
	
	
	/**
	 * @param userImage the userImage to set
	 */
	public void setUserImage(String userImage)
	{
		this.userImage = userImage;
	}
	
	
	/**
	 * @return the lynched
	 */
	public boolean isLynched()
	{
		return lynched;
	}
	
	
	/**
	 * @param lynched the lynched to set
	 */
	public void setLynched(boolean lynched)
	{
		this.lynched = lynched;
	}
	
	
	/**
	 * @return the hanged
	 */
	public boolean isHanged()
	{
		return hanged;
	}
	
	
	/**
	 * @param hanged the hanged to set
	 */
	public void setHanged(boolean hanged)
	{
		this.hanged = hanged;
	}
	
	
}
