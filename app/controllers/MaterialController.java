package controllers;

import static play.data.Form.form;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;

import org.apache.commons.io.IOUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.unauthorized;
import views.html.material.creatematerial;
import views.html.material.editmaterial;
import views.html.material.listmaterial;
import controllers.AbstractApplication.FinderKey;

public class MaterialController extends Controller {

    public static Result GO_MAIN = redirect(routes.MaterialController.list(0,
	    "name", "asc", ""));

    public static Result create() {
	if (!AuthenticationController.isLoggedIn())
	    return unauthorized(unauthorized.render(""));

	return ok(creatematerial.render(AuthenticationController.getUser(), ""));
    }

    public static Result edit(long id) {
	Form<Material> materialForm = form(Material.class).fill(
		Material.find.byId(id));

	return ok(editmaterial.render(AuthenticationController.getUser(), id, materialForm));
    }

    public static Result update(Long id) {
		Form<Material> materialForm = form(Material.class).bindFromRequest();
		if (materialForm.hasErrors())
			return badRequest("Erro! " + materialForm.errorsAsJson().toString());

		materialForm.get().update(id);
		return ok(String.format("Material #%d atualizado com sucesso!", id));
    }

    public static Result delete(long id) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	Material material = finder.selectUnique(new String[] { FinderKey.ID }, new Object[] { id });

	if (material == null) {
	    return badRequest(String.format(
		    "Material #%d não está cadastrado!", id));
	} else {
	    return ok(String.format("Material #%d removido com sucesso!", id));
	}
    }

    public static Result list(int page, String sortBy, String order,
	    String filter) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);

	return ok(listmaterial.render(AuthenticationController.getUser(), null,
		finder.page(page, 10, sortBy, order, filter), sortBy, order,
		filter));
    }
    
    public static Result list(String message) {
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);

	return ok(listmaterial.render(AuthenticationController.getUser(), message,
		finder.page(0, 10, "id", "asc", ""), "id", "asc",
		""));
    }

    public static Result upload() {
	// Vai pegar o autor da sessão, considerando que quem faz o upload são
	// os autores

	if (AuthenticationController.isLoggedIn()) {
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

	    User user = AuthenticationController.getUser();

	    if (user != null) {
		material.setAuthor(user);
		material.save();
		return list("Material enviado com sucesso!");
	    } else {
		return unauthorized("Usuário não está logado! Sessão expirada?");
	    }

	} else {
	    return unauthorized(unauthorized.render(""));
	}
    }
}
