package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Date;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;

import org.apache.commons.io.IOUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.material.*;
import views.html.unauthorized;
import controllers.AbstractApplication.ControllerKey;
import static play.data.Form.*;

public class MaterialController extends Controller {

    public static Result GO_MAIN = redirect(routes.MaterialController.list(0,
	    "name", "asc", ""));

    private static boolean isLoggedIn() {
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);

	return (auth != null && !auth.isEmpty());
    }

    public static Result create() {
	if (!isLoggedIn())
	    return unauthorized(unauthorized.render(""));

	return ok(creatematerial.render(""));
    }

    public static Result edit(long id) {
	Form<Material> materialForm = form(Material.class).fill(
		Material.find.byId(id));
	
	return ok(editmaterial.render(id, materialForm));
    }

    public static Result update(Long id) {
	Form<Material> materialForm = form(Material.class).bindFromRequest();
	if (materialForm.hasErrors()) {
	    return badRequest(editmaterial.render(id, materialForm));
	}
	
	materialForm.get().update(id);
	flash("success", "O material " + materialForm.get().getTitle()
		+ " foi atualizado.");
	
	return GO_MAIN;
    }

    public static Result delete(long id) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	
	Material material = finder.selectUnique(
		    new String[] { "id" },		//FIXME como referenciar uma coluna sem ser hard-coded?
		    new Object[] { id });
	
	if (material == null) {
	    return badRequest(String.format("Material #%d não está cadastrado!", id));
	} else {
	    return ok(String.format("Material #%d removido com sucesso!", id));
	}
    }

    public static Result list(int page, String sortBy, String order,
	    String filter) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);

	return ok(listmaterial.render(
		finder.page(page, 10, sortBy, order, filter), sortBy, order,
		filter));
    }

    public static Result upload() {
	// Vai pegar o autor da sessão, considerando que quem faz o upload são
	// os autores
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);

	if (auth != null && !auth.isEmpty()) {
	    MultipartFormData body = request().body().asMultipartFormData();
	    FilePart materialFile = body.getFile("materialFile");

	    Map<String, String[]> formContent = body.asFormUrlEncoded();
	    String[] price = formContent.get("materialPrice");
	    String[] title = formContent.get("materialTitle");
	    String[] pricePolicy = formContent.get("pricePolicyRadios");

	    PricePolicy policy = null;
	    switch (pricePolicy[0]) {
	    case "fixedValue":
		policy = PricePolicy.FIXED_VALUE;
		break;
	    case "minimumValue":
		policy = PricePolicy.MINIMUM_VALUE;
		break;
	    case "free":
		policy = PricePolicy.FREE;
		break;
	    }

	    Material material = new Material();
	    material.setTitle(title[0]);
	    material.setPricePolicy(policy);
	    material.setPrice(price[0]);

	    byte[] lob = null;
	    try {
		lob = IOUtils.toByteArray(new FileInputStream(materialFile
			.getFile()));
	    } catch (IOException e) {
		return internalServerError(e.getMessage());
	    }
	    material.setMaterialFile(lob);

	    Date now = new Date();
	    material.setCreated(now);
	    material.setModifiedAt(now);

	    FinderFactory factory = FinderFactory.getInstance();
	    IFinder<User> finder = factory.get(User.class);
	    User user = finder.selectUnique(
		    new String[] { ControllerKey.SESSION_AUTH },
		    new Object[] { auth });

	    if (user != null) {
		material.setAuthor(user);
		material.save();
		return ok("Arquivo enviado com sucesso!");
	    } else {
		return unauthorized("Usuário não está logado! Sessão expirada?");
	    }
	} else {
	    return unauthorized(unauthorized.render(""));
	}
    }
}
