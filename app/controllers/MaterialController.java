package controllers;

import static play.data.Form.form;

import com.avaje.ebean.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import org.apache.http.auth.AUTH;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import models.classes.Material;
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
					
					return list(String.format("Material \"%s\" atualizado com sucesso!", material.getTitle()));
				}
			}
			return unauthorized(ERR_EXPIRED);
		} catch (FileNotFoundException | IllegalArgumentException | ZeroPagesException e) {
			return internalServerError();
		} catch (FormatNotSupportedException e) {
			Material material = Material.find.byId(id);
			Form<Material> materialForm = form(Material.class).fill(material);
			return ok(editmaterial.render(e.getMessage(), id, materialForm));
		}
	}

	public static Result comment(Long id) {
		if (AuthenticationController.isLoggedIn()) {
			User user = AuthenticationController.getUser();
			IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
			Material material = finder.selectUnique(new String[]{FinderKey.ID}, new Object[]{id});

			if (material != null) {				
				MultipartFormData body = request().body().asMultipartFormData();
				Map<String, String[]> formContent = body.asFormUrlEncoded();
				String content = formContent.get("content")[0];
				Comment comment = new Comment();
				comment.setAuthor(user);
				comment.setCreated(new Date());
				comment.setContent(content);
				comment.save();

				String s = "INSERT INTO material_comment (material_id,comment_id) " +
						"SELECT * FROM (SELECT " + material.getId() + ", " + comment.getId() + ") AS tmp " + 
						"WHERE NOT EXISTS ("+
							"SELECT material_id FROM material_comment WHERE (material_id,comment_id) = (" + material.getId() + ", " + comment.getId() + ") "+
						") LIMIT 1;";
				SqlUpdate update = Ebean.createSqlUpdate(s);
				Ebean.execute(update);

				return redirect("/material/" + material.getId());
			}
		}
		return unauthorized(ERR_EXPIRED);
	}

	public static Result delete(long id) {
		User user = AuthenticationController.getUser();
		IFinder<Material> finder = FinderFactory.getInstance().get(Material.class);
		Material material = finder.selectUnique(new String[]{FinderKey.ID}, new Object[]{id});

		if (material == null) {
			return notFound("Material não encontrado!");
		} else if (UserUtil.isOwner(material, user) || UserController.isAdmin()) {
			material.delete();
			return list(String.format("Material \"%s\" removido com sucesso!", material.getTitle()));
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
		} catch (FormatNotSupportedException e) {
			Form<Material> materialForm = form(Material.class);
			return ok(creatematerial.render(e.getMessage(), materialForm));
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

		if (material == null || material.getMaterialFile() == null || !material.getPricePolicy().equals(Material.PricePolicy.FREE)) {
			return notFound("Material não encontrado!");
		} else {
			User user = AuthenticationController.getUser();

			// FIXME Ficou bem feio usando os getId diretamente, ao invés do setParameter
			// feito era antes, mas não funcionava de jeito nenhum...
			String s = "INSERT INTO user_has_material (user_id,material_id) " +
						"SELECT * FROM (SELECT " + user.getId() + ", " + material.getId() + ") AS tmp " + 
						"WHERE NOT EXISTS ("+
							"SELECT user_id FROM user_has_material WHERE (user_id,material_id) = (" + user.getId() + ", " + material.getId() + ") "+
						") LIMIT 1;";

			SqlUpdate update = Ebean.createSqlUpdate(s);

			Ebean.execute(update);

			response().setContentType("application/x-download");  
			response().setHeader("Content-disposition","attachment; filename=" + material.getTitle().replaceAll("\\W+", "")+ ".pdf"); 
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

    public static Result estatistica(long id) {
        FinderFactory factory = FinderFactory.getInstance();
        IFinder<Material> finder = factory.get(Material.class);
        Material material = finder.selectUnique(id);

        User user = AuthenticationController.getUser();
        if (material != null && UserUtil.isOwner(material, user)) {
            List<ObjectNode> valores = new ArrayList<ObjectNode>();
            ObjectNode jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);
            jsonValor = Json.newObject();
            jsonValor.put("valor", 0);
            valores.add(jsonValor);

            String sql = "SELECT * FROM user_has_material WHERE material_id = :mId ORDER BY when ASC";
            List<SqlRow> rows = Ebean.createSqlQuery(sql).setParameter("mId", material.getId()).findList();

            Timestamp last = null;
            for (SqlRow row : rows) {
                Timestamp timestamp = row.getTimestamp("when");
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(timestamp.getTime());

                int month = c.get(Calendar.MONTH);
                ObjectNode node = valores.get(month);
                double valor = node.get("valor").asDouble();
                valor += Double.parseDouble(material.getPrice());
                node.put("valor", valor);
            }

            ObjectNode jsonResponse = Json.newObject();
            jsonResponse.put("valores", Json.toJson(valores));

            return ok(jsonResponse);
        }

        return badRequest();
    }

}
