package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.*;

@Entity
public class EventLog extends Model {
	public String description;
	
	 @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDateTime")
	 public LocalDateTime eventTime = new LocalDateTime();
	
	public static EventLog log(String message) {
		EventLog eventLog = new EventLog();
		eventLog.description = message;
		
		eventLog.save();
		
		return eventLog;
	}

	public static List<EventLog> getLatestLoggings(int maxEntires) {
		List<EventLog> result = EventLog.find("order by eventTime desc").fetch(maxEntires);
		return result;
	}
	
	@Override
	public String toString() {
		return "EventLog<'" + description + "' - " + eventTime;
	}
}
