package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Card extends Model {
	@Required
	public int cardNumber;
	
    @ManyToOne
	public Game game;
    
	public CardStatus cardStatus = CardStatus.IN_DRAWPILE;

	
	public static Card create(Game game, int cardNumber) {
		Card card = new Card();
		card.game = game;
		card.cardNumber = cardNumber;
		game.cards.add(card);
		return card;
	}
}
