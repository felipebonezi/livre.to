package controllers;

import models.classes.Material;
import models.finders.FinderFactory;
import models.finders.IFinder;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    public static Result index() {
	// FIXME arrumar um jeito sem precisar ficar acessando o finder sempre
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	return ok(index.render(AuthenticationController.getUser(), null,
		finder.page(0, 8, "score", "desc", "")));
    }

    public static Result index(String message) {
	// FIXME arrumar um jeito sem precisar ficar acessando o finder sempre
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	return ok(index.render(AuthenticationController.getUser(), message,
		finder.page(0, 8, "score", "desc", "")));
    }
}
