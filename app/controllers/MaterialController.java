package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;

import org.apache.commons.io.IOUtils;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.material.creatematerial;
import controllers.AbstractApplication.ControllerKey;

public class MaterialController extends Controller {

    public static final boolean LOGIN_ENABLED = false; // FIXME Quando login
						       // estiver ok, remover o
						       // código que depende
						       // disso

    public static Result create() {
	return ok(creatematerial.render(""));
    }

    public static Result upload() {
	MultipartFormData body = request().body().asMultipartFormData();
	FilePart materialFile = body.getFile("materialFile");

	Map<String, String[]> formContent = body.asFormUrlEncoded();
	String[] price = formContent.get("materialPrice");
	String[] title = formContent.get("materialTitle");
	String[] pricePolicy = formContent.get("pricePolicyRadios");

	PricePolicy policy = null;
	switch (pricePolicy[0]) {
	case "fixedValueRadio":
	    policy = PricePolicy.FIXED_VALUE;
	    break;
	case "minimumValueRadio":
	    policy = PricePolicy.MINIMUM_VALUE;
	    break;
	case "forFreeRadio":
	    policy = PricePolicy.FREE;
	    break;
	}

	Material material = new Material();
	material.setTitle(title[0]);
	material.setPricePolicy(policy);
	material.setPrice(price[0]);
	byte[] lob = null;
	try {
	    lob = IOUtils.toByteArray(new FileInputStream(materialFile.getFile()));
	} catch (IOException e) {
	    return internalServerError(e.getMessage());
	}
	material.setMaterialFile(lob);

	// Vai pegar o autor da sessão, considerando que quem faz o upload são
	// os autores
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);
	if (!LOGIN_ENABLED || (auth != null && !auth.isEmpty())) {
	    FinderFactory factory = FinderFactory.getInstance();
	    IFinder<User> finder = factory.get(User.class);
	    User user = null;
	    if (LOGIN_ENABLED) {
		user = finder.selectUnique(
			new String[] { ControllerKey.SESSION_AUTH },
			new Object[] { auth });
	    } else {
		user = finder.selectUnique(1l);
	    }
	    material.setAuthor(user);
	    // TODO integrar e configurar o banco
	    material.save();

	    return ok("File uploaded");
	} else {
	    return unauthorized("Usuário não está logado!");
	}
    }
}
