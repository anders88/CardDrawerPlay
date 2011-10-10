package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;
import play.libs.Crypto;

import javax.persistence.*;

import no.anksoft.carddrawer.PlayerInfo;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class Player extends Model implements PlayerInfo {
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
	

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Player)) {
			return false;
		}
		return nullSafeEquals(name, ((Player) obj).name);
	}

	private static <T> boolean nullSafeEquals(T a, T b) {
		return (a != null) ? a.equals(b) : b != null;
	}
	
	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : -1;
	}

	@Override
	public String getName() {
		return name;
	}
}
