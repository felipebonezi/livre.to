package controllers;

import models.actions.AjaxAction;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import models.utils.UserUtil;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import java.util.Map;
import java.util.UUID;

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

            if (login != null && !login.isEmpty() &&
                    password != null && !password.isEmpty()) {
                FinderFactory factory = FinderFactory.getInstance();
                IFinder<User> finder = factory.get(User.class);
                User user = finder.selectUnique(new String[] { FinderKey.LOGIN, FinderKey.PASSWORD }, new Object[] { login, password });

                if (user != null && UserUtil.isAvailable(user)) {
                    String accessToken = UUID.randomUUID().toString();
                    user.setAccessToken(accessToken);
                    user.update();

                    Http.Session session = session();
                    session.clear();
                    session.put(ControllerKey.SESSION_AUTH, accessToken);

                    return ok("Bem-vindo, " + user.getName() + "!");
                }
            }
        }

        return unauthorized("Você não está autorizado a efetuar esta operação.");
    }

}
