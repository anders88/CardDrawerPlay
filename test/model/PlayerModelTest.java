package model;
import static org.junit.Assert.*;
import models.Player;

import org.junit.After;
import org.junit.Test;

import play.test.UnitTest;

public class PlayerModelTest extends UnitTest {

    @Test
    public void shouldSaveAndFindPlayer() {
        Player.create("Darth").save();
        assertNotNull(Player.findWithName("Darth"));
    }

    @After
    public void cleanDb() {
    	Player.deleteAll();
    }

}
