package controllers;

import static play.data.Form.form;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.MaterialUtil;
import models.utils.UserUtil;

import org.apache.commons.io.IOUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import utils.ImageUtils;
import utils.ZeroPagesException;
import views.html.unauthorized;
import views.html.material.creatematerial;
import views.html.material.editmaterial;
import views.html.material.listmaterial;

import com.avaje.ebean.Page;

import controllers.AbstractApplication.FinderKey;

public class MaterialController extends Controller {
	public static Result GO_MAIN = redirect(routes.MaterialController.list(0,
		"name", "asc", ""));

	public static Result create() {
	if (!AuthenticationController.isLoggedIn())
		return unauthorized(unauthorized.render(""));
	Form<Material> materialForm = form(Material.class);
	return ok(creatematerial.render(AuthenticationController.getUser(), "",
		materialForm));
	}

	public static Result edit(long id) {
	User user = AuthenticationController.getUser();
	Material material = Material.find.byId(id);

	if (material != null && UserUtil.isOwner(material, user)) {
		Form<Material> materialForm = form(Material.class).fill(material);

		return ok(editmaterial.render(user, "", id, materialForm));
	} else {
		return unauthorized(unauthorized
			.render("Você não tem permissão para realizar essa ação."));
	}
	}

	public static Result update(Long id) {
	try {
		if (AuthenticationController.isLoggedIn()) {
		User user = AuthenticationController.getUser();
		if (user != null) {
			MultipartFormData body = request().body().asMultipartFormData();
			Material material = MaterialUtil.generateFromForm(body);
			material.setAuthor(user);
			material.update(id);
			
			return list(String.format(
				"Material #%d atualizado com sucesso!", id));
		}
		}
		
		return unauthorized("Usuário não está logado! Sessão expirada?");
	} catch (FileNotFoundException | IllegalArgumentException
		| ZeroPagesException e) {
		return internalServerError();
	}
	}

	public static Result delete(long id) {
		User user = AuthenticationController.getUser();
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> finder = factory.get(Material.class);
		Material material = finder.selectUnique(new String[] { FinderKey.ID }, new Object[] { id });

		if (material == null) {
			return list(String.format("Material #%d não está cadastrado!", id));
		} else if (UserUtil.isOwner(material, user) || UserController.isAdmin()) {
			material.delete();
			return list(String.format("Material #%d removido com sucesso!", id));
		} else {
			return unauthorized(unauthorized.render("Você não tem permissão para realizar essa ação."));
		}
	}

	public static Result list(int page, String sortBy, String order,
		String filter) {
	User user = AuthenticationController.getUser();
	if (user == null)
		return unauthorized(unauthorized.render("TESTE"));

	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);

	Page<Material> pages = finder.getFinder().where()
		.ilike("title", "%" + filter + "%")
		.orderBy(sortBy + " " + order).fetch("author").where()
		.eq("author_id", user.getId()).findPagingList(10)
		.setFetchAhead(false).getPage(page);

	return ok(listmaterial.render(AuthenticationController.getUser(), null,
		pages, sortBy, order, filter));
	}

	public static Result list(String message) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);

	return ok(listmaterial.render(AuthenticationController.getUser(),
		message, finder.page(0, 10, "title", "asc", ""), "id", "asc", ""));
	}

	public static Result upload() {
	try {
		if (AuthenticationController.isLoggedIn()) {
		User user = AuthenticationController.getUser();
		if (user != null) {
			MultipartFormData body = request().body().asMultipartFormData();
			Material material = MaterialUtil.generateFromForm(body);
			material.setCreated(new Date());
			material.setAuthor(user);
			material.save();
			
			return list("Material enviado com sucesso!");
		}
		}
		
		return unauthorized("Usuário não está logado! Sessão expirada?");
	} catch (FileNotFoundException | IllegalArgumentException
		| ZeroPagesException e) {
		return internalServerError();
	}
	}

	public static Result renderImage(long id) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	Material material = finder.selectUnique(id);
	if (material.getMaterialThumbnail() == null)
		return ok("http://placehold.it/200x280/336600&text=livre.to");
	else
		return ok(material.getMaterialThumbnail()).as("image/png");
	}

}
