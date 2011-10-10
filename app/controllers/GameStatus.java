package controllers;

import java.util.Collection;

import no.anksoft.carddrawer.CardDealer;

import models.Game;
import models.Player;

public class GameStatus {
	private String gameName;
	private int cardsInDrawpile;
	private Collection<Integer> playerCards;

	public String hello() {
		return "Game status says hello";
	}

	public String gameName() {
		return gameName;
	}

	public static GameStatus create(Game game, Player player, CardDealer cardDealer) {
		GameStatus gameStatus = new GameStatus();
		gameStatus.gameName = game.name;
		gameStatus.cardsInDrawpile = cardDealer.numberOfCardsInDrawpile();
		gameStatus.playerCards = cardDealer.playerCards(player);
		return gameStatus;
	}

	public int cardsInDrawpile() {
		return cardsInDrawpile;
	}

	public Collection<Integer> playerCards() {
		return playerCards;
	}
}
