package controllers;

import static play.data.Form.form;

import com.avaje.ebean.*;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import models.classes.Material;
import models.classes.Category;
import models.classes.Comment;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.MaterialUtil;
import models.utils.UserUtil;
import utils.FormatNotSupportedException;
import utils.ImageUtils;
import utils.ZeroPagesException;
import views.html.unauthorized;
import views.html.category.createcategory;
import views.html.category.editcategory;
import views.html.category.listcategory;

import com.avaje.ebean.Page;

import controllers.AbstractApplication.FinderKey;

public class CategoryController extends Controller {

	private static final String ERR_UNAUTHORIZED = "Você não tem permissão para realizar essa ação.";
	private static final String ERR_EXPIRED = "Usuário não está logado! Sessão expirada?";

	public static Result create() {
		if (!AuthenticationController.isLoggedIn()) {
			return unauthorized(unauthorized.render(""));
		}

		Form<Category> categoryForm = form(Category.class);
		return ok(createcategory.render("", categoryForm));
	}

	public static Result save() {
		if (AuthenticationController.isLoggedIn()) {
			MultipartFormData body = request().body().asMultipartFormData();
			Map<String, String[]> formContent = body.asFormUrlEncoded();
			
			String name = formContent.get("name")[0];

			Category category = new Category();
			category.setName(name);
			category.save();
			
			return list("Categoria cadastrada com sucesso!");
		}

		return unauthorized(ERR_EXPIRED);
	}

	public static Result edit(long id) {
		User user = AuthenticationController.getUser();
		Category category = Category.find.byId(id);

		if (category != null) {
			Form<Category> categoryForm = form(Category.class).fill(category);
			return ok(editcategory.render("", categoryForm));
		} else {
			return unauthorized(unauthorized.render(ERR_UNAUTHORIZED));
		}
	}

	public static Result update(Long id) {
		if (AuthenticationController.isLoggedIn()) {
			MultipartFormData body = request().body().asMultipartFormData();
			Map<String, String[]> formContent = body.asFormUrlEncoded();
			
			String name = formContent.get("name")[0];

			Category category = new Category();
			category.setName(name);
			category.update(id);
			
			return list("Categoria atualizada com sucesso!");
		}

		return unauthorized(ERR_EXPIRED);
	}

	public static Result delete(long id) {
		User user = AuthenticationController.getUser();
		IFinder<Category> finder = FinderFactory.getInstance().get(Category.class);
		Category category = finder.selectUnique(new String[]{FinderKey.ID}, new Object[]{id});

		if (category == null) {
			return notFound("Categoria não encontrado!");
		} else if (UserController.isAdmin()) {
			category.delete();
			return list(String.format("Categoria \"%s\" removida com sucesso!", category.getName()));
		} else {
			return unauthorized(unauthorized.render(ERR_UNAUTHORIZED));
		}
	}

	public static Result list(int page, String sortBy, String order, String filter) {
		if (!AuthenticationController.isLoggedIn()) {
			return unauthorized(unauthorized.render(""));
		}

		User user = AuthenticationController.getUser();
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Category> finder = factory.get(Category.class);
		Page<Category> pages = finder.getFinder().where()
			.ilike("name", "%" + filter + "%")
			.orderBy(sortBy + " " + order).findPagingList(10)
			.setFetchAhead(false).getPage(page);

		return ok(listcategory.render(null, pages, sortBy, order, filter));
	}

	public static Result list(String message) {
		User user = AuthenticationController.getUser();
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Category> finder = factory.get(Category.class);

		return ok(listcategory.render(message, finder.page(), "id", "asc", ""));
	}
}
