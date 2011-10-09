package model;

import models.Game;
import models.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class GameModelTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}
	
	@Test
	public void shoulSetupAGame() {
    	Game.start("TestGame", 30);
    	assertEquals(1, Game.findAll().size());
	}
}

