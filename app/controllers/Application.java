package controllers;

import play.*;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.mvc.*;

import java.util.*;

import controllers.Secure.Security;

import models.*;

public class Application extends Controller {

    public static void index() {
    	render();
    }
    
    public static void playerCreation() {
    	render();
    }
    
    public static void createPlayer(String playerName, String password) {
    	validation.required(playerName).message("Name is required");
       	validation.required(password).message("Password is required");
       	if (Player.findWithName(playerName) != null) {
       		validation.addError("playerName", "A player with that name already exsists");
       	}
       	if (validation.hasErrors()) {
       		params.flash(); 
            validation.keep();
       		playerCreation();
       	} else {
       		Player.create(playerName, password).save();
       	    GameController.index();
       	}
    }
    

}