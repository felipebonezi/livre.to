package controllers;

import java.util.Map;
import java.util.UUID;

import models.actions.AjaxAction;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.UserUtil;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;
import views.html.login;

import controllers.AbstractApplication.ControllerKey;

/**
 * Created by felipebonezi on 27/05/14.
 */
public class AuthenticationController extends AbstractApplication {

    @With(AjaxAction.class)
    public static Result authorize() {
	Map<String, String[]> form = request().body().asFormUrlEncoded();
	if (form != null) {
	    String login = form.get(ParameterKey.LOGIN)[0];
	    String password = form.get(ParameterKey.PASSWORD)[0];

	    if (login != null && !login.isEmpty() && password != null
		    && !password.isEmpty()) {
		FinderFactory factory = FinderFactory.getInstance();
		IFinder<User> finder = factory.get(User.class);
		User user = finder.selectUnique(new String[] { FinderKey.LOGIN,
			FinderKey.PASSWORD }, new Object[] { login, password });

		if (user != null && UserUtil.isAvailable(user)) {
		    String accessToken = UUID.randomUUID().toString();
		    user.setAccessToken(accessToken);
		    user.update();

		    Http.Session session = session();
		    session.clear();
		    session.put(ControllerKey.SESSION_AUTH, accessToken);

		    return ok(index.render(AuthenticationController.getUser(), "Você efetuou login com sucesso. Bem-vindo de volta, " + user.getName() + "!"));
		}
	    }
	}

	return unauthorized("Você não está autorizado a efetuar esta operação.");
    }

    public static Result logout() {
	Http.Session session = session();
	session.clear();
	
	User user = getUser();
	if (user != null) {
	    user.setAccessToken(null);
	}
	
	return ok(index.render(AuthenticationController.getUser(), "Logout efetuado com sucesso!"));
    }

    public static Result login() {
	User user = getUser();
	if(user != null)
	    return ok(index.render(AuthenticationController.getUser(), "Você efetuou login com sucesso. Bem-vindo de volta, " + user.getName() + "!"));
	return ok(login.render()); 
    }

    public static boolean isLoggedIn() {
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);

	return (auth != null && !auth.isEmpty());
    }

    public static User getUser() {
	Http.Session session = session();
	String auth = session.get(ControllerKey.SESSION_AUTH);
	FinderFactory factory = FinderFactory.getInstance();
	IFinder<User> finder = factory.get(User.class);
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
