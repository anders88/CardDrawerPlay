package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import no.anksoft.carddrawer.CardDealer;
import no.anksoft.carddrawer.CardDealerLogger;
import no.anksoft.carddrawer.CardStatus;
import no.anksoft.carddrawer.PlayerInfo;

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
		CardDealer cardDealer = setupDealer(DbCardDealerLogger.INSTANCE);
		return GameStatus.create(this,player,cardDealer);
	}

	public CardDealer setupDealer(CardDealerLogger cardDealerLogger) {
		CardStatus[] cardStatus = new CardStatus[numberOfCards];
		Map<PlayerInfo, Set<Integer>> playerCards = new HashMap<PlayerInfo, Set<Integer>>();
		
		for (Card card : cards) {
			cardStatus[card.cardNumber-1] = card.cardStatus;
			if (card.cardStatus == CardStatus.DRAWN) {
				findOrCreate(playerCards,card.player).add(card.cardNumber);
			}
		}
		
		return new CardDealer(cardStatus, cardDealerLogger, playerCards );
	}

	private Set<Integer> findOrCreate(
			Map<PlayerInfo, Set<Integer>> playerCards, Player player) {
		Set<Integer> set = playerCards.get(player);
		if (set == null) {
			set = new HashSet<Integer>();
			playerCards.put(player, set);
		}
		return set;
	}

	public void updateCards(CardDealer cardDealer) {
		for (Card card : cards) {
			card.cardStatus = cardDealer.getCardStatus(card.cardNumber);
			PlayerInfo playerInfo = cardDealer.cardOwner(card.cardNumber);
			Player cardOwner = null;
			if (playerInfo != null) {
				cardOwner = Player.findWithName(playerInfo.getName());
			}
			card.player = cardOwner;
			card.save();
		}
		
	}

	public void removeCard(int cardNumber) {
		Card card = findCard(cardNumber);
		cards.remove(card);
		save();
		Card.<Card>findById(card.id).delete();
	}

	private Card findCard(int cardNumber) {
		for (Card card : cards) {
			if (card.cardNumber == cardNumber) {
				return card;
			}
		}
		return null;
	}

	public void updateNumberOfCards(int newNumberOfCards) {
		if (newNumberOfCards < 1) {
			throw new IllegalArgumentException("Illegal number of cards " + newNumberOfCards);
		}
		int oldNumberOfCards = numberOfCards;
		numberOfCards = newNumberOfCards;
		save();
		
		for (int newCardNumber=oldNumberOfCards+1;newCardNumber<=newNumberOfCards;newCardNumber++) {
			cards.add(Card.create(this, newCardNumber));
			save();
		}

		for (int deleteCardNumber=newNumberOfCards+1;deleteCardNumber<=oldNumberOfCards;deleteCardNumber++) {
			removeCard(deleteCardNumber);
		}
		
	}
	
}
