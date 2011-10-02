package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class Player extends Model {
	@Required
	public String name;
	
	public static Player create(String name) {
		Player player = new Player();
		player.name = name;
		return player;
	}

	public static Player findWithName(String name) {
		return Player.find("byName", name).first();
	}
}
