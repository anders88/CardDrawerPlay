package controllers;

import play.*;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.mvc.*;

import java.util.*;

import controllers.Secure.Security;

import models.*;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
    	 if(Security.isConnected()) {
             String username = Security.connected();
             renderArgs.put("user", username);
             List<Game> games = Game.findAll();
			render(username,games);
         }
    }
    
    public static void createGamePage() {
    	render();
    }
    
    public static void createGame(String gameName,Integer numberOfCards) {
    	validation.required(gameName).message("Name is required");
    	validation.required(numberOfCards).message("Number of cards is required");
    	validation.range(numberOfCards, 1, 100).message("Number of cards must be between 1 and 100 was " + numberOfCards);
    	if (validation.hasErrors()) {
    		params.flash(); 
            validation.keep();
    		createGamePage();
    	} else {
    		Game game = Game.start(gameName, numberOfCards);
    		game.save();
    	    index();
    	}
    }

}