package controllers;

import java.util.List;

import no.anksoft.carddrawer.CardDealer;

import models.DbCardDealerLogger;
import models.Game;
import models.Player;
import controllers.Secure;
import controllers.Secure.Security;
import play.mvc.*;

@With(Secure.class)
public class GameController extends Controller {
	public static void index() {
		if (Security.isConnected()) {
			String username = Security.connected();
			renderArgs.put("user", username);
			List<Game> games = Game.findAll();
			render(username, games);
		}
	}

	public static void createGamePage() {
		render();
	}

	public static void createGame(String gameName, Integer numberOfCards) {
		validation.required(gameName).message("Name is required");
		validation.required(numberOfCards).message(
				"Number of cards is required");
		validation.range(numberOfCards, 1, 100).message(
				"Number of cards must be between 1 and 100 was "
						+ numberOfCards);
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			createGamePage();
		} else {
			Game game = Game.start(gameName, numberOfCards);
			game.save();
			index();
		}
	}

	public static void showGame(Long gameId) {
		Player player = Player.findWithName(Security.connected());
		Game game = Game.findById(gameId);
		GameStatus gameStatus = game.gameStatus(player);
		render(game, player, gameStatus);
	}
	
	public static void drawCard(Long gameId) {
		Player player = Player.findWithName(Security.connected());
		Game game = Game.findById(gameId);
		CardDealer dealer = game.setupDealer(DbCardDealerLogger.INSTANCE);
		dealer.drawCard(player);
		game.updateCards(dealer);
		showGame(gameId);
	}

}
