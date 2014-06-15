package controllers;

import models.classes.Material;
import models.classes.Category;
import models.finders.FinderFactory;
import models.finders.IFinder;
import com.avaje.ebean.Page;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    public static Result index() {
    	return index(null);
    }

    public static Result index(String message) {
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		IFinder<Category> categoryFinder = FinderFactory.getInstance().get(Category.class);
		return ok(index.render(message, finder.page(), "", null, categoryFinder.selectAll()));
	}

    public static Result index(String message, int page, String filter, String categoryId) {
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> finder = factory.get(Material.class);
		IFinder<Category> categoryFinder = FinderFactory.getInstance().get(Category.class);

		Category category = null;
		if(categoryId != null && !categoryId.isEmpty()) {
			try {
				category = categoryFinder.selectUnique(Long.valueOf(categoryId));
			} catch(NumberFormatException e) {}
		}

		Page<Material> pages = null;
		if(category != null) {
			pages = finder.getFinder().where()
				.ilike("title", "%" + filter + "%")
				.orderBy("title asc").fetch("category").where()
				.eq("category_id", category.getId()).findPagingList(10)
				.setFetchAhead(false).getPage(page);
		} else {
			pages = finder.getFinder().where()
				.ilike("title", "%" + filter + "%")
				.orderBy("title asc").findPagingList(10)
				.setFetchAhead(false).getPage(page);
		}

		return ok(index.render(message, pages, filter, category, categoryFinder.selectAll()));
	}
}
