package com.ntnu.game.model.gameLogic.finale;

public class WordCount implements Comparable<WordCount>
{
	private String	word;
	private int		amount;
	
	
	/**
	 * @param word
	 * @param amount
	 */
	public WordCount(String word, int amount)
	{
		super();
		this.word = word;
		this.amount = amount;
	}
	
	
	/**
	 * @return the word
	 */
	public String getWord()
	{
		return word;
	}
	
	
	/**
	 * @param word the word to set
	 */
	public void setWord(String word)
	{
		this.word = word;
	}
	
	
	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}
	
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	
	@Override
	public int compareTo(WordCount o)
	{
		if (amount < o.amount)
			return 1;
		else if (amount > o.amount)
			return -1;
		else
			return 0;
	}
	
	
}
