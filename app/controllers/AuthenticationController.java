package controllers;

import java.util.Map;
import java.util.UUID;

import models.actions.AjaxAction;
import models.classes.Material;
import models.classes.User;
import models.classes.Category;
import models.classes.User.Group;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.UserUtil;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;
import views.html.login;
import views.html.password;
import views.html.unauthorized;
import controllers.AbstractApplication.ControllerKey;

/**
 * Created by felipebonezi on 27/05/14.
 */
public class AuthenticationController extends AbstractApplication {

	@With(AjaxAction.class)
	public static Result authorize() {
		String message = "Preencha todos os dados corretamente.";
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		if (form != null) {
			String login = form.get(ParameterKey.LOGIN)[0];
			String password = form.get(ParameterKey.PASSWORD)[0];

			if (login != null && !login.isEmpty() && password != null && !password.isEmpty()) {
				IFinder<User> finder = FinderFactory.getInstance().get(User.class);

				User user = finder.selectUnique(new String[] { FinderKey.LOGIN,
					FinderKey.PASSWORD }, new Object[] { login, password });

				if (user != null && UserUtil.isAvailable(user)) {
					String accessToken = UUID.randomUUID().toString();
					user.setAccessToken(accessToken);
					user.update();

					Http.Session session = session();
					session.clear();
					session.put(ControllerKey.SESSION_AUTH, accessToken);
					
					// FIXME arrumar um jeito sem precisar ficar acessando o finder sempre
					IFinder<Material> materialFinder = FinderFactory.getInstance().get(Material.class);
					IFinder<Category> categoryFinder = FinderFactory.getInstance().get(Category.class);
					return ok(index.render("Você efetuou login com sucesso. Bem-vindo de volta, " + user.getName() + "!", materialFinder.page(), "", null, categoryFinder.selectAll()));
				} else {
					message = "O login/senha informado estão incorretos...";
				}
			}
		}

		return unauthorized(unauthorized.render(message));
	}

	public static Result logout() {
		Http.Session session = session();
		session.clear();
		
		User user = getUser();
		if (user != null) {
			user.setAccessToken(null);
			user.update();
		}
		
		//FIXME arrumar um jeito sem precisar ficar acessando o finder sempre
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<Material> materialFinder = factory.get(Material.class);
		IFinder<Category> categoryFinder = FinderFactory.getInstance().get(Category.class);
		return ok(index.render("Logout efetuado com sucesso!", materialFinder.page(0, 8, "id", "asc", ""), "", null, categoryFinder.selectAll()));
	}

	public static Result login() {
		User user = getUser();
		if(user != null) {
			//FIXME arrumar um jeito sem precisar ficar acessando o finder sempre
			FinderFactory factory = FinderFactory.getInstance();
			IFinder<Material> materialFinder = factory.get(Material.class);
			IFinder<Category> categoryFinder = FinderFactory.getInstance().get(Category.class);
			return ok(index.render("Você efetuou login com sucesso. Bem-vindo de volta, " + user.getName() + "!", materialFinder.page(0, 8, "id", "asc", ""), "", null, categoryFinder.selectAll()));
		}
		return ok(login.render()); 
	}

	public static Result showPassword() {
		return ok(password.render(""));
	}

	public static Result recoveryPassword() {
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		if (form != null) {
			String mail = form.get(ParameterKey.MAIL)[0];

			FinderFactory factory = FinderFactory.getInstance();
			IFinder<User> finder = factory.get(User.class);
			User user = finder.selectUnique(new String[] { FinderKey.MAIL }, new Object[] { mail });
			if (user != null && UserUtil.isAvailable(user)) {
				String newPassword = UserUtil.generateAlphaNumeric();
				user.setPassword(newPassword);
				user.update();

				return ok(password.render(newPassword));
			}
		}

		return unauthorized(unauthorized.render("Não existe nenhum usuário com o e-mail informado!"));
	}

	public static boolean isLoggedIn() {
		Http.Session session = session();
		String auth = session.get(ControllerKey.SESSION_AUTH);

		return (auth != null && !auth.isEmpty() && getUser() != null);
	}

	public static User getUser() {
		Http.Session session = session();
		String auth = session.get(ControllerKey.SESSION_AUTH);
		IFinder<User> finder = FinderFactory.getInstance().get(User.class);
		User user = finder.selectUnique(
			new String[] { ControllerKey.SESSION_AUTH },
			new Object[] { auth });

		return user;
	}
	
	public static String getSessionAuth() {
		Http.Session session = session();
		String auth = session.get(ControllerKey.SESSION_AUTH);
		
		return auth;
	}
}
