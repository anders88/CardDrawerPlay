package controllers;

import models.Game;
import models.Player;

public class GameStatus {
	private String gameName;

	public String hello() {
		return "Game status says hello";
	}

	public String gameName() {
		return gameName;
	}

	public static GameStatus create(Game game, Player player) {
		GameStatus gameStatus = new GameStatus();
		gameStatus.gameName = game.name;
		return gameStatus;
	}
}
