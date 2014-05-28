package controllers;

import models.actions.AjaxAction;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import play.mvc.Result;
import play.mvc.With;

import java.util.Map;

/**
 * Created by felipebonezi on 28/05/14.
 */
public class UserController extends AbstractApplication {

    @With(AjaxAction.class)
    public static Result register() {
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        if (form != null) {
            String login = form.get(ParameterKey.LOGIN)[0];
            String password = form.get(ParameterKey.PASSWORD)[0];
            String name = form.get(ParameterKey.NAME)[0];
            String genderStr = form.get(ParameterKey.GENDER)[0];

            if (!login.isEmpty() && !password.isEmpty() && !name.isEmpty() && !genderStr.isEmpty()) {
                FinderFactory factory = FinderFactory.getInstance();
                IFinder<User> finder = factory.get(User.class);
                boolean userAlreadyExists = finder.selectUnique(new String[] { FinderKey.LOGIN }, new Object[] { login }) != null;

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
