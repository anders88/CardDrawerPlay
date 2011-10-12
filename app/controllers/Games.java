package controllers;

import play.mvc.With;
import controllers.CRUD;

@With(Secure.class)
public class Games extends CRUD {

}
