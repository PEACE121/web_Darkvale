package com.ntnu.game.model.gameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.ThreadManager;
import com.ntnu.game.model.UserEntity;
import com.ntnu.game.model.gameLogic.finale.FinaleLogic;


public class GameLogic
{
	private Long											id;
	
	private final List<UserEntity>					activeUsers					= new ArrayList<UserEntity>();
	
	private EGameState									currentGameState;
	
	private final Map<EGameState, FinaleLogic>	minigames					= new HashMap<EGameState, FinaleLogic>();
	
	private static GameLogic							instance;
	
	private static int									PLAYER_AMOUNT				= 5;
	
	private long											timerStart;
	
	private static long									COUNTDOWN_MILLISECONDS	= 10000;
	
	
	/**
	 * 
	 */
	private GameLogic()
	{
		super();
		currentGameState = EGameState.NOT_STARTED;
		// activeUsers.add(UserDao.INSTANCE.getUserById("16717695577786171977"));
		// activeUsers.add(UserDao.INSTANCE.getUserById("12112374913947174301"));
		// activeUsers.add(UserDao.INSTANCE.getUserById("11482051139213797184"));
		// activeUsers.add(UserDao.INSTANCE.getUserById("12172093410689230249"));
	}
	
	
	public int playersNeeded()
	{
		return PLAYER_AMOUNT;
	}
	
	
	public static GameLogic getInstance()
	{
		if (instance == null)
		{
			instance = new GameLogic();
		}
		return instance;
	}
	
	
	public static void reset()
	{
		instance = null;
	}
	
	
	public FinaleLogic getFinalGame()
	{
		return minigames.get(currentGameState);
	}
	
	
	public void playerReady(UserEntity user)
	{
		if (!activeUsers.contains(user))
		{
			if (currentGameState.equals(EGameState.NOT_STARTED))
			{
				activeUsers.add(user);
			}
			if (activeUsers.size() == PLAYER_AMOUNT)
			{
				currentGameState = EGameState.COUNTDOWN;
				timerStart = System.currentTimeMillis();
				// init games
				FinaleLogic finale = new FinaleLogic(activeUsers, PLAYER_AMOUNT);
				minigames.put(EGameState.FINALE, finale);
				Thread thread = ThreadManager.createBackgroundThread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(COUNTDOWN_MILLISECONDS);
							currentGameState = EGameState.FINALE;
							getFinalGame().start();
						} catch (InterruptedException ex)
						{
						}
					}
				});
				thread.start();
				currentGameState = EGameState.COUNTDOWN;
			}
		}
	}
	
	
	public String getCurrentGameState()
	{
		return currentGameState.name();
	}
	
	
	public boolean isReady()
	{
		return currentGameState == EGameState.COUNTDOWN;
	}
	
	
	public boolean isRunning()
	{
		return !(currentGameState == EGameState.NOT_STARTED) && !(currentGameState == EGameState.COUNTDOWN);
	}
	
	
	public long getTime()
	{
		return (timerStart + COUNTDOWN_MILLISECONDS) - System.currentTimeMillis();
	}
	
	
	/**
	 * @return the activeUsers
	 */
	public List<UserEntity> getActiveUsers()
	{
		return activeUsers;
	}
	
	
	public boolean isUserReady(String userId)
	{
		for (UserEntity user : activeUsers)
		{
			if (user.getGoogleUserId().equals(userId))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public String getStringRepresentationOfState()
	{
		String state = GameLogic.getInstance().getCurrentGameState();
		if (GameLogic.getInstance().getFinalGame() != null)
		{
			state += GameLogic.getInstance().getFinalGame().getCurrentStateString();
		}
		return state;
	}
	
	
}
