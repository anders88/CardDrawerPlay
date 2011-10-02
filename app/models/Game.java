package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Game extends Model {
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Player> players = new HashSet<Player>();
	public String name;
	public int numberOfCards;

	public static Game start(String name, int numberOfCards) {
		Game game = new Game();
		game.name = name;
		game.numberOfCards = numberOfCards;
		game.save();
		return game;
	}
}
