package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.DbCardDealerLogger;
import models.Game;
import models.Player;
import no.anksoft.carddrawer.CardDealer;
import play.db.jpa.JPABase;
import play.mvc.Controller;
import play.mvc.With;
import controllers.Secure.Security;

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
	
	public static void discardCard(Long gameId, Integer cardNumber) {
		Game game = Game.findById(gameId);
		
		if (validateCardNumber(game,cardNumber)) {
			CardDealer dealer = game.setupDealer(DbCardDealerLogger.INSTANCE);
			dealer.discardCard(cardNumber);
			game.updateCards(dealer);
		}
		
		showGame(gameId);
	}
	
	public static void putCardOutOfPlay(Long gameId, Integer cardNumber) {
		Game game = Game.findById(gameId);
		
		if (validateCardNumber(game,cardNumber)) {
			CardDealer dealer = game.setupDealer(DbCardDealerLogger.INSTANCE);
			dealer.putCardOutOfPlay(cardNumber);
			game.updateCards(dealer);
		}
		
		showGame(gameId);
	}

	private static boolean validateCardNumber(Game game, Integer cardNumber) {
		validation.required(cardNumber).message(
				"Card number is required");
		validation.range(cardNumber, 1, game.numberOfCards).message(
				"Number of cards must be between 1 and " + game.numberOfCards + " was "
						+ cardNumber);
		
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			return false;
		}
		return true;
	}
	
	public static void gameInfo(Long gameId) {
		
		Player player = Player.findWithName(Security.connected());
		Game game;
		if (gameId == null) {
			List<Game> findAll = Game.findAll();
			game = findAll.get(0);
		} else {
			game = Game.findById(gameId);
		}
		GameStatus gameStatus = game.gameStatus(player);
		render(gameStatus);
	}

}
