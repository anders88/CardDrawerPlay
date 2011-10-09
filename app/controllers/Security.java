package controllers;

import models.Player;

public class Security extends controllers.Secure.Security {
	static boolean authenticate(String username, String password) {
		Player player = Player.findWithName(username);
		if (player == null) return false;
        return player.validatePassword(password);
    }
}
