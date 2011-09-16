package controllers;

import play.*;
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
             render(username);
         }
    }

}