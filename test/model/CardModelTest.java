package model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.Card;
import models.Game;
import no.anksoft.carddrawer.CardStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.cache.Cache;
import play.db.jpa.JPABase;
import play.test.Fixtures;
import play.test.UnitTest;

public class CardModelTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void shouldCreateCardsWhenGameIsCreated() throws Exception {
		Game.start("Test", 5);
		assertEquals(5, Card.findAll().size());
	}
	
	@Test
	public void shouldFindCardsFromGame() throws Exception {
		Game gameOne = Game.start("GameOne", 5);
		Game.start("GameTwo", 10);
		assertEquals(15, Card.findAll().size());
		
		assertEquals(5, gameOne.cards.size());
		
		List<Card> allCards = Card.findAll();
		assertEquals(CardStatus.IN_DRAW_DECK, allCards.get(0).cardStatus); 
	}
	
	@Test
	public void shouldDeleteCard() throws Exception {
		Game gameOne = Game.start("GameOne", 5);

		assertEquals(5,gameOne.cards.size());
		assertEquals(5, Card.findAll().size());
		
		gameOne.removeCard(3);
		
		assertEquals(4,Game.<Game>findById(gameOne.id).cards.size());
		assertEquals(4, Card.findAll().size());
		
	}

}
