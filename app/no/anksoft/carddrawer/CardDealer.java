package no.anksoft.carddrawer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


public class CardDealer {

	private Random random;
	private int cardsLeft;
	private final CardStatus cardStatus[];
	private final int highest;
	private CardDealerLogger cardDealerLogger;
	
	private Map<PlayerInfo, Set<Integer>> playerCards = new Hashtable<PlayerInfo, Set<Integer>>();

	public CardDealer(int highest) {
		this.highest = highest;
		this.cardsLeft = highest;
		this.cardStatus = initCardStatus(highest);
	}
	
	

	public CardDealer(CardStatus[] cardStatus,
			CardDealerLogger cardDealerLogger,
			Map<PlayerInfo, Set<Integer>> playerCards) {
		super();
		this.random = new Random();
		this.cardStatus = cardStatus;
		this.cardDealerLogger = cardDealerLogger;
		this.playerCards = playerCards;
		this.highest = cardStatus.length;
		this.cardsLeft = calculateCardsLeft();
	}



	private int calculateCardsLeft() {
		int num = 0;
		for (CardStatus status : cardStatus) {
			if (status == CardStatus.IN_DRAW_DECK) {
				num++;
			}
		}
		return num;
	}



	private CardStatus[] initCardStatus(int highest) {
		CardStatus[] result = new CardStatus[highest];
		for (int i=0;i<highest;i++) {
			result[i] = CardStatus.IN_DRAW_DECK;
		}
		return result;
	}

	public int numberOfCardsInDrawpile() {
		return cardsLeft;
	}

	public void setRandom(Random random) {
		this.random = random;
		
	}

	public int drawCard(PlayerInfo player) {
		if (cardsLeft == 0) {
			putDiscardedCardsBackInDeck();
			if (cardsLeft == 0) {
				throw new IllegalStateException("There are no cards left");
			}
		}
		
		int randomSeed = random.nextInt(cardsLeft);
		int draw = -1;
		for (int pos=0;pos<=randomSeed;pos++) {
			draw++;
			while (cardStatus[draw] != CardStatus.IN_DRAW_DECK) {
				draw++;
			}
		} 
		cardsLeft--;
		cardStatus[draw] = CardStatus.DRAWN;
		
		int cardNo = draw+1;
		dealCard(player,cardNo);
		cardDealerLogger.drewCard(cardNo, player);
		return cardNo;
	}

	private void dealCard(PlayerInfo player, int cardNo) {
		Set<Integer> cardList = playerCards.get(player);
		if (cardList == null) {
			cardList = new HashSet<Integer>();
			playerCards.put(player, cardList);
		}
		cardList.add(cardNo);
	}

	private void putDiscardedCardsBackInDeck() {
		cardDealerLogger.shuffledDiscardPileIntoDrawPile();
		for (int i=0;i<highest-1;i++) {
			if (cardStatus[i] == CardStatus.DISCARDED) {
				cardStatus[i] = CardStatus.IN_DRAW_DECK;
				cardsLeft++;
			}
		}
	}

	public void discardCard(int cardNumber) {
		updateDiscardStatus(cardNumber, CardStatus.DISCARDED);
		cardDealerLogger.discardedCard(cardNumber);
	}

	public void putCardOutOfPlay(int cardNumber) {
		cardDealerLogger.putCardOutOfPlay(cardNumber);
		updateDiscardStatus(cardNumber, CardStatus.OUT_OF_PLAY);
	}

	private void updateDiscardStatus(int cardNumber, CardStatus discardStatus) {
		int cardIndex = calculateCardIndex(cardNumber);
		CardStatus oldStatus = cardStatus[cardIndex];
		cardStatus[cardIndex] = discardStatus;
		if (oldStatus == CardStatus.IN_DRAW_DECK) {
			cardsLeft--;
		}
		for (Set<Integer> playersCards : playerCards.values()) {
			playersCards.remove(cardNumber);
		}
	}

	private int calculateCardIndex(int cardNumber) {
		if ((cardNumber < 1) || (cardNumber > highest)) {
			throw new IllegalArgumentException("Card number must between 1 and " + highest);
		}
		int cardIndex = cardNumber-1;
		return cardIndex;
	}

	public void setCardDealerLogger(CardDealerLogger cardDealerLogger) {
		this.cardDealerLogger = cardDealerLogger;
	}

	public Set<Integer> playerCards(PlayerInfo player) {
		return playerCards.get(player);
	}

	public Set<Integer> discardedCards() {
		return makeListOfCardsWithStatus(CardStatus.DISCARDED);
	}

	public Set<Integer> outOfPlayCards() {
		return makeListOfCardsWithStatus(CardStatus.OUT_OF_PLAY);
	}

	public Set<Integer> cardsInDrawpile() {
		return makeListOfCardsWithStatus(CardStatus.IN_DRAW_DECK);
	}

	private Set<Integer> makeListOfCardsWithStatus(CardStatus status) {
		HashSet<Integer> discarded = new HashSet<Integer>();
		for (int cardIndex=0;cardIndex<cardStatus.length;cardIndex++) {
			if (cardStatus[cardIndex] == status) {
				discarded.add(cardIndex+1);
			}
		}
		return discarded;
	}



	public PlayerInfo cardOwner(int cardNo) {
		if (cardStatus[cardNo-1] != CardStatus.DRAWN) {
			return null;
		}
		for (Entry<PlayerInfo, Set<Integer>> entry : playerCards.entrySet()) {
			if (entry.getValue().contains(cardNo)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public CardStatus getCardStatus(int cardNumber) {
		return cardStatus[cardNumber-1];
	}
	


}
