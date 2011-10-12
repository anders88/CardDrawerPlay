package controllers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import no.anksoft.carddrawer.CardDealer;

import models.Game;
import models.Player;

public class GameStatus {
	private String gameName;
	private int cardsInDrawpile;
	private SortedSet<Integer> playerCards;
	private SortedSet<Integer> discardedCards;
	private SortedSet<Integer> outOfPlayCards;

	public String gameName() {
		return gameName;
	}

	public static GameStatus create(Game game, Player player, CardDealer cardDealer) {
		GameStatus gameStatus = new GameStatus();
		gameStatus.gameName = game.name;
		gameStatus.cardsInDrawpile = cardDealer.numberOfCardsInDrawpile();
		gameStatus.playerCards = new TreeSet<Integer>(cardDealer.playerCards(player));
		gameStatus.discardedCards = new TreeSet<Integer>(cardDealer.discardedCards());
		gameStatus.outOfPlayCards = new TreeSet<Integer>(cardDealer.outOfPlayCards());
		
		return gameStatus;
	}

	public int cardsInDrawpile() {
		return cardsInDrawpile;
	}

	public Collection<Integer> playerCards() {
		return playerCards;
	}

	public Collection<Integer> discardedCards() {
		return discardedCards;
	}

	public Collection<Integer> outOfPlayCards() {
		return outOfPlayCards;
	}
}
