package models;

import play.db.DB;
import no.anksoft.carddrawer.CardDealerLogger;
import no.anksoft.carddrawer.PlayerInfo;

public class DbCardDealerLogger implements CardDealerLogger {
	
	public static final  DbCardDealerLogger INSTANCE = new DbCardDealerLogger();

	@Override
	public void drewCard(int cardNumber, PlayerInfo player) {
		EventLog.log(player.getName() + " drew card");
	}

	@Override
	public void discardedCard(int cardNumber) {
		EventLog.log("Card discarded: " + cardNumber);

	}

	@Override
	public void putCardOutOfPlay(int cardNumber) {
		EventLog.log("Card put out of play: " + cardNumber);
	}

	@Override
	public void shuffledDiscardPileIntoDrawPile() {
		EventLog.log("Shuffled discard pile into draw pile");
	}

}
