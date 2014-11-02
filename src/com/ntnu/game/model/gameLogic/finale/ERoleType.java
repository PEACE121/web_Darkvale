package com.ntnu.game.model.gameLogic.finale;

public enum ERoleType
{
	WEREWOLF,
	VILLAGER,
	
	/** Villager, protects a villager each night **/
	CLERIC,
	
	/** Villager, can see the card of one player **/
	WITCH_HUNTER,
	
	/** Werewolf, if he dies in the next round two villagers will die **/
	ZEALOUS_CULTIST
}
