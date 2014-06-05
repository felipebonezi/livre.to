package controllers;

import play.mvc.*;
import play.mvc.Result;

import views.html.*;

public class Application extends Controller {
    public static Result index() {
	 return ok(index.render(AuthenticationController.getUser(), null));
    }
    
    public static Result index(String message) {
	 return ok(index.render(AuthenticationController.getUser(), message));
   }
}
