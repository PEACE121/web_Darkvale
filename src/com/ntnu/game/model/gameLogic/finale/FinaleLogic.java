package com.ntnu.game.model.gameLogic.finale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.appengine.api.ThreadManager;
import com.ntnu.game.model.UserEntity;
import com.ntnu.game.model.gameLogic.GameLogic;
import com.ntnu.game.model.gameLogic.IMinigame;
import com.ntnu.game.model.gameLogic.minigame.AngryCitizenMinigameDirtyWater;
import com.ntnu.ui.JSP;


public class FinaleLogic
{
	private final Map<String, Role>			roles								= new HashMap<String, Role>();
	
	private final Map<String, TRole>			tRoles							= new HashMap<String, TRole>();
	
	private final Map<String, UserEntity>	players							= new HashMap<String, UserEntity>();
	
	private int										round								= 0;
	
	private EFinaleState							currentState					= EFinaleState.NIGHT;
	
	private static final long					SLIDE_MILLISECONDS			= 2000;
	
	private long									slideTimerStart				= 0;
	
	private final Map<String, String>		chosenPlayers					= new HashMap<String, String>();
	private final Map<String, String>		chosenPlayersDoubleLynch	= new HashMap<String, String>();
	
	private final List<String>					reflection						= new ArrayList<String>();
	
	private final Map<String, String>		specialInformation			= new HashMap<String, String>();
	
	private final List<String>					generalInformation			= new ArrayList<String>();
	
	private final int								AMOUNT_NEEDED_FOR_LOVERS	= 6;
	
	private boolean								townCrierAlive					= true;
	
	private boolean								doubleLynchNextRound			= false;
	
	private final List<SocialMediaType>		socialMedias					= new ArrayList<SocialMediaType>();
	
	private SocialMediaType						currentSocialMedia;
	
	private final List<IMinigame>				minigames						= new ArrayList<IMinigame>();
	
	private IMinigame								currentMinigame;
	
	
	public FinaleLogic(List<UserEntity> activePlayers, int playersAmount)
	{
		socialMedias.add(new SocialMediaType("Facebook"));
		socialMedias.add(new SocialMediaType("Mail"));
		socialMedias.add(new SocialMediaType("Google Docs"));
		socialMedias.add(new SocialMediaType("It's learning"));
		socialMedias.add(new SocialMediaType("Twitter"));
		currentSocialMedia = socialMedias.get((int) Math.floor(socialMedias.size() * Math.random()));
		for (UserEntity player : activePlayers)
		{
			players.put(player.getGoogleUserId(), player);
		}
		List<Role> blankRoles = new ArrayList<Role>();
		blankRoles.add(new Role("Werewolf", ERoleType.WEREWOLF, false));
		blankRoles.add(new Role("Werewolf", ERoleType.WEREWOLF, false));
		blankRoles.add(new Role("Villager", ERoleType.VILLAGER, true));
		blankRoles.add(new Role("Villager", ERoleType.VILLAGER, true));
		if (playersAmount > 4)
			blankRoles.add(new Role("Cleric", ERoleType.CLERIC, true));
		if (playersAmount > 5)
			blankRoles.add(new Role("Werewolf", ERoleType.WEREWOLF, false));
		if (playersAmount > 6)
			blankRoles.add(new Role("Witch Hunter", ERoleType.WITCH_HUNTER, true));
		if (playersAmount > 7)
			blankRoles.add(new Role("Villager", ERoleType.VILLAGER, true));
		if (playersAmount > 8)
			blankRoles.add(new Role("Zealous Cultist", ERoleType.ZEALOUS_CULTIST, false));
		Collections.shuffle(blankRoles);
		for (int i = 0; i < activePlayers.size(); i++)
		{
			roles.put(activePlayers.get(i).getGoogleUserId(), blankRoles.get(i));
		}
		List<TRole> blankTRoles = new ArrayList<TRole>();
		blankTRoles.add(new TRole("townCrier", ETRoleType.TOWN_CRIER));
		if (playersAmount >= AMOUNT_NEEDED_FOR_LOVERS)
		{
			blankTRoles.add(new TRole("lover1", ETRoleType.LOVERS));
			blankTRoles.add(new TRole("lover2", ETRoleType.LOVERS));
		}
		for (int i = blankTRoles.size(); i < activePlayers.size(); i++)
		{
			blankTRoles.add(new TRole("nothing", ETRoleType.NOTHING));
		}
		Collections.shuffle(blankTRoles);
		for (int i = 0; i < activePlayers.size(); i++)
		{
			tRoles.put(activePlayers.get(i).getGoogleUserId(), blankTRoles.get(i));
		}
		
		minigames.add(new AngryCitizenMinigameDirtyWater(this));
		currentMinigame = minigames.get((int) Math.floor(minigames.size() * Math.random()));
	}
	
	
	private void resetReady()
	{
		for (UserEntity player : players.values())
		{
			player.setReady(false);
		}
	}
	
	
	public long slideTime()
	{
		return (slideTimerStart + SLIDE_MILLISECONDS) - System.currentTimeMillis();
	}
	
	
	public long phaseTime()
	{
		return (slideTimerStart + currentState.ms) - System.currentTimeMillis();
	}
	
	
	public String getTCardFile(String userId)
	{
		for (Entry<String, TRole> user : tRoles.entrySet())
		{
			if (user.getKey().equals(userId))
			{
				switch (user.getValue().getRole())
				{
					case LOVERS:
						return "images/cardTLover01.png";
					case TOWN_CRIER:
						return "images/cardTTownCrier.png";
					case NOTHING:
						return "images/cardTEmpty.png";
				}
				return "";
			}
		}
		return "";
	}
	
	
	public String getCardFile(String userId)
	{
		if (players.get(userId).isHanged() || players.get(userId).isLynched())
		{
			return "images/cardESpectre.png";
		}
		for (Entry<String, Role> user : roles.entrySet())
		{
			if (user.getKey().equals(userId))
			{
				switch (user.getValue().getRole())
				{
					case ZEALOUS_CULTIST:
						return "images/cardGCultist.png";
					case WEREWOLF:
						return "images/cardGWerewolf01.png";
					case VILLAGER:
						return "images/cardIVillager01.png";
					case CLERIC:
						return "images/cardICleric.png";
					case WITCH_HUNTER:
						return "images/cardIWitchHunter.png";
				}
				return "";
			}
		}
		return "";
	}
	
	
	public String currentPhase()
	{
		return currentState.name;
	}
	
	
	public void start()
	{
		currentState = EFinaleState.INTRO;
		System.out.println("Start is invoked. Players:");
		for (UserEntity player : players.values())
		{
			System.out.println(player.getName() + ", " + roles.get(player.getGoogleUserId()).getName() + ", "
					+ tRoles.get(player.getGoogleUserId()).getName());
		}
		slideTimerStart = System.currentTimeMillis();
		Thread thread = ThreadManager.createBackgroundThread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					while (true)
					{
						Thread.sleep(1000);
						
						if ((System.currentTimeMillis() - slideTimerStart) > currentState.getMs())
						{
							System.out.println("State: " + currentState.name);
							resetReady();
							generalInformation.clear();
							specialInformation.clear();
							reflection.clear();
							switch (currentState)
							{
								case INTRO:
									currentState = EFinaleState.NIGHT;
									break;
								case NIGHT:
									System.out.println("Night -> Day");
									currentState = EFinaleState.DAY;
									GameLogic.getInstance().getFinalGame().nightDayTransition();
									break;
								case DAY:
									System.out.println("Day -> Reflection");
									currentState = EFinaleState.REFLECTION;
									GameLogic.getInstance().getFinalGame().dayNightTransition();
									break;
								case REFLECTION:
									System.out.println("Reflection -> Night");
									currentState = EFinaleState.NIGHT;
									break;
								case LAST_REFLECTION:
									break;
							}
							if (currentState != EFinaleState.LAST_REFLECTION && currentState != EFinaleState.REFLECTION)
							{
								generalInformation.add("Use only the cooperation technology " + currentSocialMedia.getName()
										+ " in this round.");
							}
							chosenPlayers.clear();
							chosenPlayersDoubleLynch.clear();
							doubleLynchNextRound = false;
							if (!townCrierAlive)
							{
								System.out.println("Town crier is dead!");
								generalInformation.add("The Town Crier is dead!");
							}
							slideTimerStart = System.currentTimeMillis();
							if (checkForWin())
							{
								System.out.println("Win");
								currentState = EFinaleState.LAST_REFLECTION;
							}
						}
					}
				} catch (InterruptedException ex)
				{
				}
			}
		});
		thread.start();
	}
	
	
	public void forceNextPhase(int inMs)
	{
		slideTimerStart = System.currentTimeMillis() - currentState.getMs() + inMs;
	}
	
	
	private boolean checkForWin()
	{
		boolean onlyVillagers = true;
		boolean onlyWerewolves = true;
		boolean onlyLovers = (players.size() >= AMOUNT_NEEDED_FOR_LOVERS);
		for (UserEntity player : players.values())
		{
			if (!player.isHanged() && !player.isLynched())
			{
				switch (roles.get(player.getGoogleUserId()).getRole())
				{
					case WEREWOLF:
					case ZEALOUS_CULTIST:
						onlyVillagers = false;
						break;
					case VILLAGER:
					case CLERIC:
					case WITCH_HUNTER:
						onlyWerewolves = false;
						break;
				}
				if (tRoles.get(player.getGoogleUserId()).getRole() != ETRoleType.LOVERS)
				{
					onlyLovers = false;
				}
			}
		}
		if (onlyLovers)
		{
			generalInformation.add("The Lovers won!");
		}
		if (onlyVillagers)
		{
			generalInformation.add("The Villagers won!");
		}
		if (onlyWerewolves)
		{
			generalInformation.add("The Werevolves won!");
		}
		return onlyLovers || onlyVillagers || onlyWerewolves;
	}
	
	
	private void nightDayTransition()
	{
		currentMinigame = minigames.get((int) Math.floor(minigames.size() * Math.random()));
		for (String player : players.keySet())
		{
			for (String info : currentMinigame.getSpecialInformation(player))
			{
				addToSpecialInformation(player, info);
			}
			for (String info : currentMinigame.getGeneralDescription())
			{
				addToSpecialInformation(player, info);
			}
		}
		
		List<ERoleType> limitedUsers = new ArrayList<ERoleType>();
		limitedUsers.add(ERoleType.ZEALOUS_CULTIST);
		limitedUsers.add(ERoleType.WEREWOLF);
		String toLynch = votedPlayer(chosenPlayers, limitedUsers);
		String toLynch2 = votedPlayer(chosenPlayersDoubleLynch, limitedUsers);
		if (toLynch.equals(""))
		{
			generalInformation.add("The Werewolves could not decide who to devour. So everybody is still alive!");
		} else
		{
			String cleric = "";
			for (String player : players.keySet())
			{
				if (roles.get(player).getRole() == ERoleType.CLERIC)
				{
					cleric = player;
				}
			}
			if (doubleLynchNextRound)
			{
				generalInformation.add("The Werewolves were allowed to devour two villagers last round.");
				if ((chosenPlayers.containsKey(cleric) && chosenPlayers.get(cleric).equals(toLynch2)))
				{
					generalInformation.add("The Cleric rescued successfully.");
				} else
				{
					generalInformation.add(players.get(toLynch2).getName() + " was devoured!. He was a "
							+ roles.get(toLynch2).getName() + ".");
					players.get(toLynch2).setLynched(true);
					playerDied(toLynch2);
				}
			}
			if (chosenPlayers.containsKey(cleric) && chosenPlayers.get(cleric).equals(toLynch))
			{
				generalInformation.add("The Cleric rescued successfully.");
			} else
			{
				generalInformation.add(players.get(toLynch).getName() + " was devoured!. He was a "
						+ roles.get(toLynch).getName() + ".");
				players.get(toLynch).setLynched(true);
				playerDied(toLynch);
			}
		}
		
		limitedUsers.clear();
		limitedUsers.add(ERoleType.WITCH_HUNTER);
		String toInformAbout = votedPlayer(chosenPlayers, limitedUsers);
		for (UserEntity player : players.values())
		{
			if (roles.get(player.getGoogleUserId()).getRole() == ERoleType.WITCH_HUNTER)
			{
				if (toInformAbout.equals(""))
				{
					addToSpecialInformation(player.getGoogleUserId(), "You have not interrogated any player last night.");
				} else
				{
					addToSpecialInformation(player.getGoogleUserId(), "The player '" + players.get(toInformAbout).getName()
							+ "' is a " + roles.get(toInformAbout).getName() + ".");
				}
			}
		}
		System.out.println("Round: " + round);
		round++;
	}
	
	
	private void dayNightTransition()
	{
		String toHang = votedPlayer(chosenPlayers);
		currentSocialMedia = socialMedias.get((int) Math.floor(socialMedias.size() * Math.random()));
		Map<String, Integer> points = currentMinigame.evaluate();
		int heighest = 0;
		List<String> bestPlayers = new ArrayList<String>();
		for (Entry<String, Integer> playerPoints : points.entrySet())
		{
			System.out.println(playerPoints.getKey() + " -> " + playerPoints.getValue());
			if (playerPoints.getValue() > heighest)
			{
				heighest = playerPoints.getValue();
				bestPlayers.clear();
				bestPlayers.add(playerPoints.getKey());
			} else if (playerPoints.getValue() == heighest)
			{
				bestPlayers.add(playerPoints.getKey());
			}
		}
		
		for (String bestPlayer : bestPlayers)
		{
			String randomPlayer = new ArrayList<String>(players.keySet()).get((int) Math.floor(Math.random()
					* (players.size())));
			String hisProfession = roles.get(randomPlayer).getName();
			if (Math.random() < 0.5)
			{
				hisProfession = roles.get(bestPlayer).getName();
			}
			addToSpecialInformation(
					bestPlayer,
					"You were one of the best players in the minigame of last round. Your reward: "
							+ players.get(randomPlayer).getName() + " is probably a " + hisProfession);
		}
		currentMinigame.reset();
		if (toHang.equals(""))
		{
			generalInformation.add("It is a draw. No one is hung.");
		} else
		{
			generalInformation.add(players.get(toHang).getName() + " was hung. He was a " + roles.get(toHang).getName()
					+ ".");
			players.get(toHang).setHanged(true);
			playerDied(toHang);
		}
	}
	
	
	private String votedPlayer(Map<String, String> chosenOnes)
	{
		List<ERoleType> limitedUsers = new ArrayList<ERoleType>();
		limitedUsers.add(ERoleType.CLERIC);
		limitedUsers.add(ERoleType.VILLAGER);
		limitedUsers.add(ERoleType.WEREWOLF);
		limitedUsers.add(ERoleType.WITCH_HUNTER);
		limitedUsers.add(ERoleType.ZEALOUS_CULTIST);
		return votedPlayer(chosenOnes, limitedUsers);
	}
	
	
	private String votedPlayer(Map<String, String> chosenOnes, List<ERoleType> limitedUsers)
	{
		Map<String, Integer> votes = new HashMap<String, Integer>();
		for (Entry<String, String> chosenPlayer : chosenOnes.entrySet())
		{
			if (limitedUsers.contains(roles.get(chosenPlayer.getKey()).getRole()))
			{
				if (!votes.containsKey(chosenPlayer.getValue()))
				{
					votes.put(chosenPlayer.getValue(), 1);
				}
				votes.put(chosenPlayer.getValue(), votes.get(chosenPlayer.getValue()) + 1);
			}
		}
		String maxVotesId = null;
		int votesAmount = 0;
		boolean draw = false;
		for (Entry<String, Integer> v : votes.entrySet())
		{
			if (maxVotesId == null || votesAmount < v.getValue())
			{
				votesAmount = v.getValue();
				maxVotesId = v.getKey();
				draw = false;
			} else if (votesAmount == v.getValue())
			{
				draw = true;
			}
		}
		if (maxVotesId == null)
		{
			draw = true;
		}
		if (draw)
		{
			return "";
		} else
		{
			return maxVotesId;
		}
	}
	
	
	private void playerDied(String userId)
	{
		if (roles.get(userId).getRole() == ERoleType.ZEALOUS_CULTIST)
		{
			doubleLynchNextRound = true;
		}
		if (tRoles.get(userId).getRole() == ETRoleType.TOWN_CRIER)
		{
			townCrierAlive = false;
		}
	}
	
	
	/**
	 * 
	 * @param userId
	 * @param headline
	 * @param action
	 * @param accuseUserId
	 * @param redirectURL
	 * @return
	 */
	public String getUserSelection(String userId, String headline, String action, String accuseUserId,
			String redirectURL, boolean grayOutDeadOnes)
	{
		String response = "<div class='row'><div class='col-md-12'><div style='text-align:center;'><h1>" + headline
				+ "</h1></div></div></div>\n";
		response += "<div class='row'><div class='container'>\n";
		for (UserEntity user : players.values())
		{
			response += " <div class='col-xs-1' style='min-width:150px;width:150px;'>\n";
			response += "   <a style='"
					+ (grayOutDeadOnes && (user.isHanged() || user.isLynched() || user.getGoogleUserId().equals(userId)) ? "pointer-events: none; cursor: default; background-color:gray;"
							: "") + (accuseUserId.equals(user.getGoogleUserId()) ? " background:red;" : "") + "' href='/"
					+ action + (action.contains("?") ? "&" : "?") + "user=" + user.getGoogleUserId()
					+ "&type=USER_SELECTION&redirect=" + redirectURL + "' class='thumbnail'>\n";
			response += "     <img class='img-rounded' style='min-height:80px;height:80px;min-width:80px;width:80px;background-image:url("
					+ user.getUserImage()
					+ "); background-size: 100% auto;background-repeat: no-repeat;'"
					+ (user.isLynched() ? " src='images/statusDevoured.png'"
							: (user.isHanged() ? " src='images/statusLynched.png'" : "src=''")) + " />\n";
			String name = user.getName();
			if (name.length() > 10)
			{
				name = name.substring(0, 10) + "...";
			}
			response += "    <div style='font-variant: small-caps;' >" + name + "</div>\n";
			response += "   </a>\n";
			response += " </div>\n";
		}
		response += "</div></div>\n";
		return response;
	}
	
	
	/**
	 * 
	 * @param userId
	 * @param headline
	 * @param action
	 * @param accuseUserId
	 * @param redirectURL
	 * @return
	 */
	public String getTextarea(Task task)
	{
		String response = "<div class='row'><div class='col-md-12'><div style='text-align:center;'><h1>"
				+ task.getTaskText() + "</h1></div></div></div>\n";
		
		response += "<div style='margin: auto; max-width: 500px;'>";
		response += "<div id='reflectionChat'>" + getReflectionChatText() + "</div>";
		response += "<div class='row'><div class='col-md-12'>";
		response += " <form name='input' action='/" + task.getRedirectURL() + "' method='get'>";
		response += "  <input type='hidden' name='type' value='" + task.getTaskType().name() + "' /> ";
		response += "  <input type='hidden' name='redirect' value='" + JSP.PHASE + "' /> ";
		response += "  <textarea class='span6' style='width:100%' rows='5' name='user' required></textarea>";
		response += "  <button type='submit' class='btn btn-default'>Submit</button>";
		response += " </form>";
		response += "</div></div>";
		response += "</div>";
		return response;
	}
	
	
	public String getReflectionChatText()
	{
		String response = "";
		for (String reflectionPoint : reflection)
		{
			response += reflectionPoint + "<br><br>";
		}
		return response;
	}
	
	
	public List<Task> getNightTaskRole(String userId)
	{
		List<Task> tasks = new ArrayList<Task>();
		switch (roles.get(userId).getRole())
		{
			case WEREWOLF:
			case ZEALOUS_CULTIST:
				tasks.add(new Task("Which player do you want to devour?", "playerChosen", ETaskType.USER_SELECTION));
				if (doubleLynchNextRound)
				{
					tasks.add(new Task("Which player do you want to devour also?", "playerChosen?doubleLynch=true",
							ETaskType.USER_SELECTION));
				}
				break;
			case WITCH_HUNTER:
				tasks.add(new Task("Which player do you want to interrogate?", "playerChosen", ETaskType.USER_SELECTION));
				break;
			case CLERIC:
				tasks.add(new Task("Which player do you want to protect?", "playerChosen", ETaskType.USER_SELECTION));
				break;
			default:
				break;
		}
		return tasks;
	}
	
	
	public String getMinigameTaskRoleText(String userId)
	{
		return currentMinigame.getCustomGameHTMLCode(userId);
	}
	
	
	public String getTaskRoleText(String userId)
	{
		List<Task> tasks = new ArrayList<Task>();
		switch (currentState)
		{
			case NIGHT:
				tasks.addAll(getNightTaskRole(userId));
				break;
			case DAY:
				if (!players.get(userId).isHanged() && !players.get(userId).isLynched())
				{
					tasks.add(new Task("Which player do you want to hang?", "playerChosen", ETaskType.USER_SELECTION));
				} else
				{
					addToSpecialInformation(userId, "You are dead");
				}
				break;
			case REFLECTION:
				tasks.add(new Task("COOPERATION TECHNOLOGY REFLECT QUESTION", "reflection", ETaskType.TEXTAREA));
				break;
			case LAST_REFLECTION:
				tasks.add(new Task("FINAL REFLECTION", "reflection", ETaskType.TEXTAREA));
				break;
			default:
				break;
		}
		if (tasks.size() == 0)
			return "<h1>Nothing to do!<h1>";
		else
		{
			String tasksString = "";
			for (Task task : tasks)
			{
				switch (task.getTaskType())
				{
					case USER_SELECTION:
						tasksString += getUserSelection(userId, task.getTaskText(), task.getRedirectURL(),
								(chosenPlayers.containsKey(userId) ? chosenPlayers.get(userId) : ""), JSP.PHASE, true);
						break;
					case TEXTAREA:
						tasksString += getTextarea(task);
						break;
					default:
						break;
				}
				
			}
			return tasksString;
		}
	}
	
	
	public List<WordCount> generateWordCloud()
	{
		String input = "Seit minuten geht nix mehr  Die Zombies fressen wohl nicht nur Gehirne   ^^ sagst du mir heute abend bescheid, wenn du wieder da bist   kannst ruhig eher kommen  ich lege den schlüssel für dein zimmer in unseren speiseschrank  ins unterste fach  nach ganz hinten irgendwo  und auf deinem bett stehen kartoffeln  die müssen eine stunde in den ofen  anleitung liegt drauf  kann man nix falsch machen  OK. Mache ich  die können ab 17 uhr in den ofen  also wenn du später kommst halt später  nur halt so, dass sie nicht vor 18 uhr fertig sind  ok? Ol Ok der Drucker verzweifelt immer noch an der mega PDF... musste erstmal wieder papier klauen^  oh mann dann hat er 30 seiten gedruckt und jetzt druckt er nur noch weiße seiten, egal was ich versuche also komplett weiß  geil er haut die seiten einfach durch  sehr tintensparend  dein bier war nicht versclosse  die kartoffeln lagen dadrauf  und haben den stöpsel geöffnet welchen? den roten oben?  den für die luft  jo  also das ganze  war draussen oh shit haste wieder drauf gemacht?  jop  kp wie das pasiert is  die kartoffeln müssen iwie drauf gefallen sein  warum auch immer die überhaupt da gelagert wurden  bin los  achja  da ligt n ü ei in der steckerleiste  ok  Wednesday 5:37pm  Hey  Wie lange sind sie schon drin? Hey ca. 20 min  Ich bin so gegen 6 da. Muss dann noch schnell den Rest kochen wenn die Kartoffeln fertig sind und dann gibt es essen  wir brauchen noch so ein Glas eingelegte Gurken!  Dann ist ja gut ok, ich hole die Gurken bis gleich  Die Kartoffeln sehen sehr lecker aus  Wednesday 7:39pm  http://www.fitnessmagnet.com/  Kann ich aber auch holen wenn du schon einkaufen warst  komme ja eh am bun prix  vorbei  Ok  Bis gleich  Ein Joghurt wäre noch super  Ohne frucht  Naturjoghurt Thursday 2:23pm  Hast du vom Benedikt Schmälz das zeug für bier brauen? Ne. Gregor  Ok  Weil der erzählt dauernd vom hier  Bier Thursday 5:22pm  Konnte mein Schlüssel im auto sein  Jackentasche  Thursday 6:48pm So Bin auf dem Weg  okjii  bring wärme mit  ich bin schon kurz vor exitus Oh nein. Ich beeile mich  besser is es Friday 10:43am  http://mobile.chefkoch.de/rezepte/m137951059636874/Yogurette-Muffins.html a few seconds ago http://www.ntnu.no/kart/gloeshaugen/kjelhuset/";
		Map<String, WordCount> wordAmount = new HashMap<String, WordCount>();
		input = input.replace(".", "");
		input = input.replace(",", "");
		for (String word : input.split(" "))
		{
			if (!wordAmount.containsKey(word))
			{
				wordAmount.put(word, new WordCount(word, 0));
			}
			wordAmount.get(word).setAmount(wordAmount.get(word).getAmount() + 1);
		}
		wordAmount.remove("");
		List<WordCount> words = new ArrayList<WordCount>(wordAmount.values());
		Collections.sort(words);
		return words.subList(0, 30);
	}
	
	
	public void playerChosen(String choosingPlayer, String chosenPlayer, String doubleLynch)
	{
		if (doubleLynch.equals("true"))
		{
			chosenPlayersDoubleLynch.put(choosingPlayer, chosenPlayer);
		} else
		{
			chosenPlayers.put(choosingPlayer, chosenPlayer);
		}
	}
	
	
	public String getSpecialInformation(String userId)
	{
		String special = "";
		if (specialInformation.containsKey(userId))
		{
			special = specialInformation.get(userId);
		}
		if (!roles.get(userId).isVillager())
		{
			String werewolves = "";
			for (Entry<String, Role> player : roles.entrySet())
			{
				if (!player.getValue().isVillager())
				{
					werewolves += players.get(player.getKey()).getName() + ", ";
				}
			}
			werewolves = werewolves.substring(0, werewolves.length() - 2);
			if (!special.equals(""))
			{
				special += "<br>";
			}
			special += "The Werewolves are: " + werewolves;
		}
		return special;
	}
	
	
	private void addToSpecialInformation(String userId, String text)
	{
		if (!text.equals(""))
		{
			if (specialInformation.containsKey(userId))
			{
				specialInformation.put(userId, specialInformation.get(userId) + "<br>" + text);
			} else
			{
				specialInformation.put(userId, text);
			}
		}
	}
	
	
	public List<String> getGeneralInformation()
	{
		List<String> general = new ArrayList<String>(generalInformation);
		general.add("Use " + currentSocialMedia + " to discuss!");
		return generalInformation;
	}
	
	
	/**
	 * @return the currentMinigame
	 */
	public IMinigame getCurrentMinigame()
	{
		return currentMinigame;
	}
	
	
	/**
	 * @return the roles
	 */
	public Map<String, Role> getRoles()
	{
		return roles;
	}
	
	
	/**
	 * @return the tRoles
	 */
	public Map<String, TRole> gettRoles()
	{
		return tRoles;
	}
	
	
	/**
	 * @return the players
	 */
	public Map<String, UserEntity> getPlayers()
	{
		return players;
	}
	
	
	/**
	 * @return the currentState
	 */
	public EFinaleState getCurrentState()
	{
		return currentState;
	}
	
	
	/**
	 * @return the currentState
	 */
	public String getCurrentStateString()
	{
		return currentState.name;
	}
	
	
	public void playerReady(String userId)
	{
		players.get(userId).setReady(true);
		boolean allPlayersReady = true;
		for (UserEntity player : players.values())
		{
			if (!player.isReady())
			{
				allPlayersReady = false;
			}
		}
		if (allPlayersReady)
		{
			System.out.println("All players ready");
			forceNextPhase(10000);
		}
	}
	
	
	public boolean isUserReady(String userId)
	{
		return players.get(userId).isReady();
	}
	
	
	public void addToRefelction(String userId, String text)
	{
		reflection.add(players.get(userId).getName() + ": <br>" + text);
	}
	
}
