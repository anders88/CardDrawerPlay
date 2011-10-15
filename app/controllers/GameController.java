package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.DbCardDealerLogger;
import models.EventLog;
import models.Game;
import models.Player;
import no.anksoft.carddrawer.CardDealer;
import play.cache.Cache;
import play.db.jpa.JPABase;
import play.mvc.Controller;
import play.mvc.With;
import controllers.Secure.Security;

@With(Secure.class)
public class GameController extends Controller {
	
	private static LinkedList<String> diceThrows = new LinkedList<String>();
	private static Random random = new Random();

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

	public static void doAdmin() {
		EventLog.log(Security.connected() + " entered admin pages");
        render("CRUD/index.html");
	}

	public static void showEventLog() {
		List<String> log = formatLog(EventLog.getLatestLoggings(100));
		render(log);
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
			EventLog.log("Game started " + game.name + " - cards " + numberOfCards);
			index();
		}
	}

	public static void showGame(Long gameId) {
		Player player = Player.findWithName(Security.connected());
		Game game = Game.findById(gameId);
		GameStatus gameStatus = game.gameStatus(player);
		List<String> diceThrowInfo=getDiceThrows();
		render(game, player, gameStatus, diceThrowInfo);
	}
	
	private static LinkedList<String> getDiceThrows() {
		return diceThrows;
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
		List<String> diceThrowInfo=getDiceThrows();
		render(gameStatus,diceThrowInfo);
	}
	
	private static List<String> formatLog(List<EventLog> log) {
		List<String> result = new ArrayList<String>();
		for (EventLog eventLog : log) {
			result.add(formatTime(eventLog.eventTime) + " : " + eventLog.description);
		}
		
		return result;
	}

	private static String formatTime(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		return formatter.print(time);
	}
	
	public static void updateNumberOfCards(Long gameId, Integer numberOfCards) {
		validation.required(numberOfCards).message(
				"Number of cards is required");
		validation.range(numberOfCards, 1, 100).message(
				"Number of cards must be between 1 and 100 was "
						+ numberOfCards);
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			Game game = Game.findById(gameId);
			game.updateNumberOfCards(numberOfCards);
			EventLog.log(Security.connected() + " updated number of cards to " + numberOfCards);
		}
		showGame(gameId);
	}
	
	public static void throwDice(Long gameId) {
		int diceThrowNumber=random.nextInt(6)+1;
		if (diceThrows.size() > 2) diceThrows.pop();
		String diceThrow = formatTime(new LocalDateTime()) + " - "  + Security.connected() + " threw dice " + diceThrowNumber;
		diceThrows.addLast(diceThrow);
		showGame(gameId);
	}
 
}
