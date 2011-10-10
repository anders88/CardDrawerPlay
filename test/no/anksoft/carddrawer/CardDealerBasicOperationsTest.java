package no.anksoft.carddrawer;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import junit.framework.Assert;

import no.anksoft.carddrawer.CardDealer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class CardDealerBasicOperationsTest {
	private CardDealer cardDealer = new CardDealer(10);
	private Random random = mock(Random.class);
	private PlayerInfo player = mock(PlayerInfo.class);
	private CardDealerLogger cardDealerLogger = mock(CardDealerLogger.class);

	
	@Test
	public void shouldHaveAllCardsInDrawPileFromTheStart() throws Exception {
		assertThat(cardDealer.numberOfCardsInDrawpile()).isEqualTo(10);
	}
	
	@Test
	public void shouldDrawACard() throws Exception {
		when(random.nextInt(10)).thenReturn(3);
		assertThat(cardDealer.drawCard(player)).isEqualTo(4);
		assertThat(cardDealer.numberOfCardsInDrawpile()).isEqualTo(9);
		verify(cardDealerLogger).drewCard(4,player);
	}

	
	@Test
	public void shouldNotDrawTheSameCardMoreThanOnce() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(3);
		
		assertThat(cardDealer.drawCard(player)).isEqualTo(4);
		PlayerInfo playerTwo = mock(PlayerInfo.class);
		
		when(playerTwo.getName()).thenReturn("PlayerTwo");
		assertThat(cardDealer.drawCard(playerTwo)).isEqualTo(5);

		assertThat(cardDealer.numberOfCardsInDrawpile()).isEqualTo(8);
		
		InOrder order = inOrder(random);
		
		order.verify(random).nextInt(10);
		order.verify(random).nextInt(9);
		verify(cardDealerLogger).drewCard(4, player);
		verify(cardDealerLogger).drewCard(5, playerTwo);
		verify(cardDealerLogger,never()).shuffledDiscardPileIntoDrawPile();
	}
	
	@Test
	public void shouldThrowIllegalStateExceptionWhenThereAreNoMoreCards() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(0);
		
		drawCards(10);

		try {
			cardDealer.drawCard(player);
			Assert.fail("Expected exception");
		} catch (IllegalStateException e) {
		}
		
	}
	
	@Test
	public void shouldPutDiscardedCardsBackIntoDeckWhenCardsRunOut() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(0);
		
		drawCards(10);

		cardDealer.discardCard(8);
		verify(cardDealerLogger).discardedCard(8);
		
		assertThat(cardDealer.drawCard(player)).isEqualTo(8);
		verify(cardDealerLogger).shuffledDiscardPileIntoDrawPile();
	}
	
	@Test
	public void shouldNotReshuffleCardsPutOutOfPlay() throws Exception {
		when(random.nextInt(anyInt())).thenReturn(0);
		
		drawCards(10);

		cardDealer.discardCard(8);
		cardDealer.putCardOutOfPlay(7);
		
		assertThat(cardDealer.drawCard(player)).isEqualTo(8);
		verify(cardDealerLogger).discardedCard(8);
		verify(cardDealerLogger).putCardOutOfPlay(7);
		
	}
	
	@Test
	public void shouldGiveDiscardedCards() throws Exception {
		cardDealer.discardCard(5);
		cardDealer.discardCard(7);
		
		assertThat(cardDealer.discardedCards()).containsOnly(5,7);
	}
	
	@Test
	public void shouldGiveListOfCardThatIsOutOfPlay() throws Exception {
		cardDealer.putCardOutOfPlay(3);
		cardDealer.putCardOutOfPlay(6);
		
		assertThat(cardDealer.outOfPlayCards()).containsOnly(3,6);
	}
	
	@Test
	public void shouldGiveListOfCardThatIsInDeck() throws Exception {
		assertThat(cardDealer.cardsInDrawpile()).containsOnly(1,2,3,4,5,6,7,8,9,10);
	}

	private void drawCards(int times) {
		for (int i=0;i<times;i++) {
			cardDealer.drawCard(player);
		}
		
	}

	@Before
	public void setup() {
		cardDealer.setRandom(random);
		cardDealer.setCardDealerLogger(cardDealerLogger);
		when(player.getName()).thenReturn("PlayerOne");
	}
}
