package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;
import play.libs.Crypto;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class Player extends Model {
	@Required
	public String name;
	
	@Required
	public String password;
	
	public static Player create(String name, String password) {
		Player player = new Player();
		player.name = name;
		player.password = Crypto.passwordHash(password);
		return player;
	}

	public static Player findWithName(String name) {
		return Player.find("byName", name).first();
	}

	public boolean validatePassword(String givenPassword) {
		return password.equals(Crypto.passwordHash(givenPassword));
	}
}
