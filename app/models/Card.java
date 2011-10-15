package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import no.anksoft.carddrawer.CardStatus;

import java.util.*;

@Entity
public class Card extends Model {
	@Required
	public int cardNumber;
	
    @ManyToOne
	public Game game;
    
    @ManyToOne
    public Player player;
    
	public CardStatus cardStatus = CardStatus.IN_DRAW_DECK;

	
	public static Card create(Game game, int cardNumber) {
		Card card = new Card();
		card.game = game;
		card.cardNumber = cardNumber;
		game.cards.add(card);
		return card;
	}
	
	@Override
	public String toString() {
		return "Card<" + cardNumber + " : " + cardStatus + " (Game " + game.id + ")>";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Card)) {
			return false;
		}
		Card other = (Card) obj;
		return nullSafeEquals(cardNumber, other.cardNumber) &&
				nullSafeEquals(game, other.game);
	}

	private static <T> boolean nullSafeEquals(T a, T b) {
		return (a != null) ? a.equals(b) : b != null;
	}
	
	@Override
	public int hashCode() {
		return new Integer(cardNumber).hashCode();
	}
	
}
