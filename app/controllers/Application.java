package controllers;

import models.classes.Material;
import models.finders.FinderFactory;
import models.finders.IFinder;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    public static Result index() {
    	return index(null);
    }

    public static Result index(String message) {
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		
		return ok(index.render(message, finder.page()));
    }
}
