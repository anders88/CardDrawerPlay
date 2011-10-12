package model;

import java.util.List;

import models.EventLog;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPABase;
import play.test.Fixtures;
import play.test.UnitTest;

public class EventLogTest extends UnitTest {
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}

	
	@Test
	public void shouldCreateLogEntry() throws Exception {
		EventLog.log("Test one");
		List<EventLog> loggings = EventLog.findAll();
		assertEquals(1, loggings.size());
		
		assertEquals("Test one", loggings.get(0).description);
		assertNotNull(loggings.get(0).eventTime);
	}
	
	@Test
	public void shouldOrderAndCut() throws Exception {
		EventLog oneDayAgo = createTestLog(-1);
		EventLog twoDaysAgo = createTestLog(-2);
		EventLog threeDaysAgo = createTestLog(-3);
		
		List<EventLog> loggings = EventLog.getLatestLoggings(2);
		Assertions.assertThat(loggings).containsExactly(oneDayAgo, twoDaysAgo).excludes(threeDaysAgo);
	}


	private EventLog createTestLog(int offsetDays) {
		EventLog log = EventLog.log("Dummy");
		log.eventTime = log.eventTime.plusDays(offsetDays);
		log.save();
		return log;
	}
	
}
