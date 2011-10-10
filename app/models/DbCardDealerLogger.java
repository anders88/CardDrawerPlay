package models;

import play.db.DB;
import no.anksoft.carddrawer.CardDealerLogger;
import no.anksoft.carddrawer.PlayerInfo;

public class DbCardDealerLogger implements CardDealerLogger {
	
	public static final  DbCardDealerLogger INSTANCE = new DbCardDealerLogger();

	@Override
	public void drewCard(int cardNumber, PlayerInfo player) {

	}

	@Override
	public void discardedCard(int cardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putCardOutOfPlay(int cardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shuffledDiscardPileIntoDrawPile() {
		// TODO Auto-generated method stub

	}

}
