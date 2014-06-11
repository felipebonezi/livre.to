package controllers;

import models.actions.AjaxAction;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import views.html.unauthorized;
import views.html.user.listuser;
import views.html.register;

import java.util.Map;

/**
 * Created by felipebonezi on 28/05/14.
 */
public class UserController extends AbstractApplication {

    public static Result GO_MAIN = redirect(routes.UserController.list(0,
	    "user", "asc", ""));

    public static Result list(int page, String sortBy, String order,
	    String filter) {
	if (!isLoggedIn() && isAdmin())
	    return unauthorized(unauthorized.render(""));
	
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<User> finder = factory.get(User.class);

	return ok(listuser.render(AuthenticationController.getUser(), finder.page(page, 10, sortBy, order, filter),
		sortBy, order, filter));
    }

    // TODO tela de gerência de usuários só pode ser acessada por Administradores. Use esse método!
    public static boolean isAdmin() {
	String auth = AuthenticationController.getSessionAuth();
	
	if (auth != null && !auth.isEmpty()) {
	    FinderFactory factory = FinderFactory.getInstance();
	    IFinder<User> finder = factory.get(User.class);
	    User user = finder.selectUnique(
		    new String[] { ControllerKey.SESSION_AUTH },
		    new Object[] { auth });

	    if (user != null) {
		return user.getGroups().contains(User.Group.ADMINISTRATOR);
	    } 
	}
	return false;
    }

    private static boolean isLoggedIn() {
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);

	return (auth != null && !auth.isEmpty());
    }

    public static Result edit() {
	// TODO
	return ok("TODO");
    }

    public static Result create() {
	// TODO
	return ok(register.render(""));
    }

    @With(AjaxAction.class)
    public static Result register() {
	Map<String, String[]> form = request().body().asFormUrlEncoded();
	if (form != null) {
	    String login = form.get(ParameterKey.LOGIN)[0];
	    String password = form.get(ParameterKey.PASSWORD)[0];
	    String name = form.get(ParameterKey.NAME)[0];
	    String genderStr = form.get(ParameterKey.GENDER)[0];

	    if (!login.isEmpty() && !password.isEmpty() && !name.isEmpty()
		    && !genderStr.isEmpty()) {
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<User> finder = factory.get(User.class);
		boolean userAlreadyExists = finder.selectUnique(
			new String[] { FinderKey.LOGIN },
			new Object[] { login }) != null;

		if (!userAlreadyExists) {
		    User.Gender gender = User.Gender.valueOf(genderStr);

		    User user = new User();
		    user.setLogin(login);
		    user.setPassword(password);
		    user.setName(name);
		    user.setGender(gender);
		    user.setStatus(User.Status.ACTIVE);
		    user.save();

		    return ok("O cadastro foi realizado com sucesso!");
		}
	    }
	}

	return unauthorized("Você não está autorizado a efetuar esta operação.");
    }
    
}
