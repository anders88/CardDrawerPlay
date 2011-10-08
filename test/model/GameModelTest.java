package model;

import models.Game;
import models.Player;

import org.junit.After;
import org.junit.Test;

import play.test.UnitTest;

public class GameModelTest extends UnitTest {
	@Test
	public void shoulSetupAGame() {
    	Game.start("TestGame", 30);
    	assertEquals(1, Game.findAll().size());
	}
	
	@After
    public void cleanDb() {
    	Player.deleteAll();
    	Game.deleteAll();
    }
}

