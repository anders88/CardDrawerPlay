package no.anksoft.carddrawer;

public interface CardDealerLogger {

	void drewCard(int cardNumber, PlayerInfo player);

	void discardedCard(int cardNumber);

	void putCardOutOfPlay(int cardNumber);

	void shuffledDiscardPileIntoDrawPile();

}
