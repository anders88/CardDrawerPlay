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
    

}