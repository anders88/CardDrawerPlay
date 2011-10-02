package model;
import static org.junit.Assert.*;
import models.Player;

import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Test;

import play.test.UnitTest;

public class PlayerModelTest extends UnitTest {

    @Test
    public void shouldSaveAndFindPlayer() {
        Player.create("Darth").save();
        Assertions.assertThat(Player.findWithName("Darth")).isNotNull();
    }

    @After
    public void cleanDb() {
    	Player.deleteAll();
    }

}
