package model;
import static org.junit.Assert.*;
import models.Player;

import org.junit.After;
import org.junit.Test;

import play.test.UnitTest;

public class PlayerModelTest extends UnitTest {

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

    @After
    public void cleanDb() {
    	Player.deleteAll();
    }

}
