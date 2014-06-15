package controllers;

import models.actions.AjaxAction;
import models.classes.Material;
import models.classes.User;
import models.finders.FinderFactory;
import models.finders.IFinder;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import views.html.unauthorized;
import views.html.user.listuser;
import views.html.register;
import views.html.editor;
import views.html.index;

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

		return ok(listuser.render(finder.page(page, 10, sortBy, order, filter), 
									sortBy, order, filter));
	}

	// TODO tela de gerência de usuários só pode ser acessada por
	// Administradores. Use esse método!
	public static boolean isAdmin() {
		String auth = AuthenticationController.getSessionAuth();

		if (auth != null && !auth.isEmpty()) {
			FinderFactory factory = FinderFactory.getInstance();
			IFinder<User> finder = factory.get(User.class);
			User user = finder.selectUnique(
					new String[] { ControllerKey.SESSION_AUTH },
					new Object[] { auth });

			if (user != null) {
				return user.getGroups().contains(User.Group.ADMINISTRATOR) || user.getLogin().equals("admin");
			}
		}
		return false;
	}

	private static boolean isLoggedIn() {
		Http.Session session = session();
		String auth = session.get(ControllerKey.SESSION_AUTH);

		return (auth != null && !auth.isEmpty());
	}

	public static Result edit(long userId) {
	    	FinderFactory factory = FinderFactory.getInstance();
		IFinder<User> finder = factory.get(User.class);

		User userToEdit = finder.selectUnique(
				new String[] { FinderKey.ID },
				new Object[] { userId });
		return ok(editor.render(userToEdit));
	}

	@With(AjaxAction.class)
	public static Result editor() {
		String message = "Preencha todos os dados corretamente.";
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		User user = AuthenticationController.getUser();

		String passwordatual = user.getPassword();

		if (form != null) {
			String name = form.get(ParameterKey.NAME)[0];
			String mail = form.get(ParameterKey.MAIL)[0];

			String passwordold = form.get(ParameterKey.PASSWORDOLD)[0];
			String passwordnew = form.get(ParameterKey.PASSWORDNEW)[0];
			String passwordnew2 = form.get(ParameterKey.PASSWORDNEW2)[0];

			if (!name.isEmpty() && !mail.isEmpty()) {				
				if ((passwordnew.isEmpty() && passwordnew2.isEmpty() && passwordold.isEmpty()) || (!passwordnew.isEmpty() && !passwordnew2.isEmpty() && !passwordold.isEmpty())) {
					if ( (passwordnew.isEmpty() && passwordnew2.isEmpty() && passwordold.isEmpty()) || passwordatual.equals(passwordold)) {
						if ((passwordnew.isEmpty() && passwordnew2.isEmpty() && passwordold.isEmpty()) || passwordnew.equals(passwordnew2)) {
							FinderFactory factory = FinderFactory.getInstance();
							IFinder<User> finder = factory.get(User.class);

							boolean mailAlreadyExists = finder.selectUnique(
									new String[] { FinderKey.MAIL },
									new Object[] { mail }) != null;

							if (!mailAlreadyExists
									|| mail.equals(user.getMail())) {
								user.setName(name);
								user.setMail(mail);
								if(!passwordnew.isEmpty()) {
									user.setPassword(passwordnew);
								}
								user.update();

								IFinder<Material> materialFinder = factory
										.get(Material.class);
								return ok(index.render("Usuário editado com sucesso!",
										materialFinder.page(0, 8, "id", "asc",
												"")));
							} else {
								message = "Já existe um usuário cadastrado com os dados fornecidos.";
							}
						} else {
							message = "Os campos Nova Senha não são iguais.";
						}
					} else {
						message = "A senha atual está incorreta.";
					}
				} else {
					message = "Todos os campos Senha devem ser preenchidos.";
				}
			}
		}

		return unauthorized(unauthorized.render(message));
	}

	public static Result create() {
		return ok(register.render());
	}

	@With(AjaxAction.class)
	public static Result register() {
		String message = "Preencha todos os dados corretamente.";
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		if (form != null) {
			String login = form.get(ParameterKey.LOGIN)[0];
			String password = form.get(ParameterKey.PASSWORD)[0];
			String name = form.get(ParameterKey.NAME)[0];
			String genderStr = form.get(ParameterKey.GENDER)[0];
			String mail = form.get(ParameterKey.MAIL)[0];

			if (!login.isEmpty() && !password.isEmpty() && !name.isEmpty()
					&& !genderStr.isEmpty() && !mail.isEmpty()) {
				FinderFactory factory = FinderFactory.getInstance();
				IFinder<User> finder = factory.get(User.class);
				boolean userAlreadyExists = finder.selectUnique(
						new String[] { FinderKey.LOGIN },
						new Object[] { login }) != null;

				boolean mailAlreadyExists = finder.selectUnique(
						new String[] { FinderKey.MAIL }, new Object[] { mail }) != null;

				if (!userAlreadyExists && !mailAlreadyExists) {
					User.Gender gender = User.Gender.valueOf(genderStr);

					User user = new User();
					user.setLogin(login);
					user.setPassword(password);
					user.setName(name);
					user.setGender(gender);
					user.setStatus(User.Status.ACTIVE);
					user.setMail(mail);
					user.save();

					IFinder<Material> materialFinder = factory
							.get(Material.class);
					return ok(index.render("Usuário foi criado com sucesso!",
							materialFinder.page(0, 8, "id", "asc", "")));
				} else {
					message = "Já existe um usuário cadastrado com os dados fornecidos.";
				}
			}
		}

		return unauthorized(unauthorized.render(message));
	}

    public static Result remove(long id) {
        String message = null;

        FinderFactory factory = FinderFactory.getInstance();
        IFinder<User> finder = factory.get(User.class);
        User user = finder.selectUnique(id);

        if (user != null && isAdmin()) {
            User userChanged = new User();
            userChanged.setStatus(User.Status.REMOVED);
            userChanged.update(user.getId());

            IFinder<Material> materialFinder = factory
                    .get(Material.class);

            return ok(index.render("O usuário (" + user.getName() + ") foi removido com sucesso.",
                    materialFinder.page(0, 8, "id", "asc", "")));
        } else {
            message = "Você não tem permissão para remover este usuário.";
        }

        return unauthorized(unauthorized.render(message));
    }

}
