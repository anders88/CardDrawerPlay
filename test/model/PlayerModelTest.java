package model;
import static org.junit.Assert.*;
import models.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class PlayerModelTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}
	
    @Test
    public void shouldSaveAndFindPlayer() {
        Player.create("Darth","password").save();
        assertNotNull(Player.findWithName("Darth"));
    }
    
    @Test
	public void shouldValidatePassword() throws Exception {
    	Player player = Player.create("Darth","password").save();
    	assertTrue(player.validatePassword("password"));
    	assertFalse(player.validatePassword("wrong"));
	}

}
