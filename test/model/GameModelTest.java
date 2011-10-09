package model;

import models.Game;
import models.Player;

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
}

