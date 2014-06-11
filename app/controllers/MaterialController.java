package controllers;

import static play.data.Form.form;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.avaje.ebean.Page;
import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;

import models.utils.UserUtil;
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
        User user = AuthenticationController.getUser();
        Material material = Material.find.byId(id);

        if (material != null && UserUtil.isOwner(material, user)) {
            Form<Material> materialForm = form(Material.class).fill(
                    material);

            return ok(editmaterial.render(user, id, materialForm));
        } else {
            return unauthorized(unauthorized.render("Você não tem permissão para realizar essa ação."));
        }
    }

    public static Result update(Long id) {
		Form<Material> materialForm = form(Material.class).bindFromRequest();
		if (materialForm.hasErrors())
			return badRequest("Erro! " + materialForm.errorsAsJson().toString());

		materialForm.get().update(id);
		return ok(String.format("Material #%d atualizado com sucesso!", id));
    }

    public static Result delete(long id) {
        User user = AuthenticationController.getUser();

	FinderFactory factory = FinderFactory.getInstance();
	IFinder<Material> finder = factory.get(Material.class);
	Material material = finder.selectUnique(new String[] { FinderKey.ID }, new Object[] { id });

	if (material == null) {
        return badRequest(String.format(
                "Material #%d não está cadastrado!", id));
    } else if (UserUtil.isOwner(material, user) || UserController.isAdmin()) {
        return ok(String.format("Material #%d removido com sucesso!", id));
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

        Page<Material> pages = finder.getFinder()
                .where()
                .ilike("title", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("author")
                .where().eq("author_id", user.getId())
                .findPagingList(10)
                .setFetchAhead(false)
                .getPage(page);

	return ok(listmaterial.render(AuthenticationController.getUser(), null,
		pages, sortBy, order,
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

    public static Result renderImage(long id) {
        FinderFactory factory = FinderFactory.getInstance();
        IFinder<Material> finder = factory.get(Material.class);
        Material material = finder.selectUnique(id);
        if (material.getMaterialFile() == null)
            return ok("http://placehold.it/200x280/336600&text=livre.to");
        else
            return ok(material.getMaterialFile()).as("image/jpg");
    }

}
