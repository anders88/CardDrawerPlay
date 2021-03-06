package no.anksoft.carddrawer;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class CardDealerPlayerOperationTest {
	private CardDealer cardDealer = new CardDealer(10);
	private Random random = mock(Random.class);
	private CardDealerLogger cardDealerLogger = mock(CardDealerLogger.class);
	
	private PlayerInfo mockPlayer(String name) {
		PlayerInfo mock = mock(PlayerInfo.class);
		when(mock.getName()).thenReturn(name);
		return mock;
	}

	@Test
	public void shouldKeepTrackOfDrawnCards() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(0);
		PlayerInfo playerOne = mockPlayer("PlayerOne");
		PlayerInfo playerTwo = mockPlayer("PlayerTwo");

		cardDealer.drawCard(playerOne);
		cardDealer.drawCard(playerTwo);
		cardDealer.drawCard(playerOne);

		assertThat(cardDealer.playerCards(playerOne)).containsOnly(1, 3);
		assertThat(cardDealer.playerCards(playerTwo)).containsOnly(2);
	}

	@Test
	public void shouldHandleDiscardedCards() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(0);
		PlayerInfo playerOne = mockPlayer("PlayerOne");

		cardDealer.drawCard(playerOne);
		cardDealer.drawCard(playerOne);
		cardDealer.drawCard(playerOne);

		cardDealer.discardCard(2);
		cardDealer.putCardOutOfPlay(3);

		assertThat(cardDealer.playerCards(playerOne)).containsOnly(1);

	}
	
	@Test
	public void shouldIndicateCorrectOwnerOfCard() throws Exception {
		PlayerInfo playerOne = mockPlayer("PlayerOne");
		PlayerInfo playerTwo = mockPlayer("PlayerTwo");
		when(random.nextInt(anyInt())).thenReturn(0);
		
		cardDealer.drawCard(playerOne);
		cardDealer.drawCard(playerTwo);
		
		assertThat(cardDealer.cardOwner(1)).isEqualTo(playerOne);
		assertThat(cardDealer.cardOwner(2)).isEqualTo(playerTwo);
		assertThat(cardDealer.cardOwner(3)).isNull();
		
	}


	@Before
	public void setup() {
		cardDealer.setRandom(random);
		cardDealer.setCardDealerLogger(cardDealerLogger);
	}

}
