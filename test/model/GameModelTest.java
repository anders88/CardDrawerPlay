package model;

import static org.mockito.Mockito.mock;

import java.util.Set;

import models.Card;
import models.Game;
import models.Player;
import no.anksoft.carddrawer.CardDealer;
import no.anksoft.carddrawer.CardDealerLogger;
import no.anksoft.carddrawer.CardStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.GameStatus;

import play.test.Fixtures;
import play.test.UnitTest;

public class GameModelTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}
	
	@Test
	public void shouldSetupAGame() {
    	Game.start("TestGame", 30);
    	assertEquals(1, Game.findAll().size());
	}
	
	@Test
	public void shouldDeliverCorrectGameStatusOnStartup() throws Exception {
		Game game = Game.start("TestGame", 10);
		Player player = Player.create("Player1", "pw");
		GameStatus gameStatus = game.gameStatus(player);
		
		assertEquals("TestGame", gameStatus.gameName());
		
	}
	
	private Card findCard(Game game, int cardNumber) {
		for (Card card : game.cards) {
			if (card.cardNumber == cardNumber) {
				return card;
			}
		}
		return null;
	}

	
	@Test
	public void shouldReturnCorrectOwnersOfCard() throws Exception {
		Game game = Game.start("TestGame", 10);
		Player player = Player.create("Player1", "pw").save();
		Card card = findCard(game, 3);
		card.cardStatus = CardStatus.DRAWN;
		card.player = player;
		card.save();
		
		CardDealer dealer = game.setupDealer(mock(CardDealerLogger.class));
		
		assertEquals(player, dealer.cardOwner(3));
	}
}

