package controllers;

import static play.data.Form.form;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.avaje.ebean.Page;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.MaterialUtil;
import models.utils.UserUtil;
import utils.ImageUtils;
import utils.ZeroPagesException;
import views.html.unauthorized;
import views.html.material.creatematerial;
import views.html.material.editmaterial;
import views.html.material.listmaterial;
import views.html.material.detalhesmaterial;

import com.avaje.ebean.Page;

import controllers.AbstractApplication.FinderKey;

public class MaterialController extends Controller {

	private static final String ERR_UNAUTHORIZED = "Você não tem permissão para realizar essa ação.";
	private static final String ERR_EXPIRED = "Usuário não está logado! Sessão expirada?";

	private static final String MIMETYPE_PNG = "image/png";
	private static final String MIMETYPE_PDF = "application/pdf";

	public static Result create() {
		if (!AuthenticationController.isLoggedIn()) {
			return unauthorized(unauthorized.render(""));
		}

		Form<Material> materialForm = form(Material.class);
		return ok(creatematerial.render("", materialForm));
	}

	public static Result edit(long id) {
		User user = AuthenticationController.getUser();
		Material material = Material.find.byId(id);

		if (material != null && UserUtil.isOwner(material, user)) {
			Form<Material> materialForm = form(Material.class).fill(material);
			return ok(editmaterial.render("", id, materialForm));
		} else {
			return unauthorized(unauthorized.render(ERR_UNAUTHORIZED));
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
					
					return list(String.format("Material #%d atualizado com sucesso!", id));
				}
			}
			return unauthorized(ERR_EXPIRED);
		} catch (FileNotFoundException | IllegalArgumentException | ZeroPagesException e) {
			return internalServerError();
		}
	}

	public static Result delete(long id) {
		User user = AuthenticationController.getUser();
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		Material material = finder.selectUnique(new String[]{FinderKey.ID}, new Object[]{id});

		if (material == null) {
			return notFound("Material não encontrado!");
		} else if (UserUtil.isOwner(material, user) || UserController.isAdmin()) {
			material.delete();
			return list(String.format("Material #%d removido com sucesso!", id));
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
		IFinder<Material> finder = factory.get(Material.class);
		Page<Material> pages = finder.getFinder().where()
			.ilike("title", "%" + filter + "%")
			.orderBy(sortBy + " " + order).fetch("author").where()
			.eq("author_id", user.getId()).findPagingList(10)
			.setFetchAhead(false).getPage(page);

//        IFinder<User> finderU = factory.get(User.class);
//        Page<User> pagesC = finderU.getFinder().where()
//                .ilike("title", "%" + filter + "%")
//                .orderBy(sortBy + " " + order)
//                .fetch("materials").where().eq("user_id", user.getId())
//                .findPagingList(10)
//                .setFetchAhead(false).getPage(page);



		return ok(listmaterial.render(null, pages, sortBy, order, filter, user.getMaterials()));
	}

	public static Result list(String message) {
		User user = AuthenticationController.getUser();
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> finder = factory.get(Material.class);

		return ok(listmaterial.render(message, finder.page(), "id", "asc", "", user.getMaterials()));
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

			return unauthorized(ERR_EXPIRED);
		} catch (FileNotFoundException | IllegalArgumentException | ZeroPagesException e) {
			return internalServerError();
		}
	}

	public static Result renderImage(long id) {
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		Material material = finder.selectUnique(id);

		if (material == null || material.getMaterialThumbnail() == null) {
			return ok(Application.class.getResourceAsStream("/public/images/placeholder.png")).as(MIMETYPE_PNG);
		} else {
			return ok(material.getMaterialThumbnail()).as(MIMETYPE_PNG);
		}
	}

	public static Result download(long id) {
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		Material material = finder.selectUnique(id);

		if (material == null || material.getMaterialFile() == null) {
			return notFound("Material não encontrado!");
		} else {
			return ok(material.getMaterialFile()).as(MIMETYPE_PDF);
		}
	}

	public static Result detalhe(Long id) {
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> finder = factory.get(Material.class);
		Material material = finder.selectUnique(id);

		if (material != null) {
			return ok(detalhesmaterial.render(material));
		}

		return notFound("Material não encontrado!");
	}

	public static Result rate(Long id, boolean upvote) {
		Result result = internalServerError();

		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> finder = factory.get(Material.class);
		Material material = finder.selectUnique(id);

		if (material != null) {
			if (upvote) {
				material.upvote();
			} else {
				material.downvote();
			}
			material.update();
			result = ok();
		} else {
			result = notFound("Material não encontrado!");
		}

		return result;
	}
}
