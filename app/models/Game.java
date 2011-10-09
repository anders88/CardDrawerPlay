package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import controllers.GameStatus;

import java.util.*;

@Entity
public class Game extends Model {
	@OneToMany(cascade=CascadeType.PERSIST)
	public Set<Card> cards = new HashSet<Card>();

	public String name;
	public int numberOfCards;

	public static Game start(String name, int numberOfCards) {
		Game game = setupGame(name, numberOfCards);
		game.save();
		return game;
	}

	public static Game setupGame(String name, int numberOfCards) {
		if (numberOfCards < 1) {
			throw new IllegalArgumentException("Illegal number of cards" + numberOfCards);
		}
		Game game = new Game();
		game.name = name;
		game.numberOfCards = numberOfCards;
		for (int cardNumber=1;cardNumber<=numberOfCards;cardNumber++) {
			game.cards.add(Card.create(game, cardNumber));
		}
		return game;
	}

	public GameStatus gameStatus(Player player) {
		return GameStatus.create(this,player);
	}
}
