package controllers;

import models.actions.AjaxAction;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class AbstractApplication extends Controller {

    protected static final class ControllerKey {
        public static final String SESSION_AUTH = "access_token";
    }

    public static final class FinderKey {
        public static final String ID = "id";
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
        public static final String MAIL = "mail";
    }

    public static final class ParameterKey {
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
		public static final String PASSWORDOLD = "passwordold";
		public static final String PASSWORDNEW = "passwordnew";
		public static final String PASSWORDNEW2 = "passwordnew2";
        public static final String NAME = "name";
        public static final String GENDER = "gender";
        public static final String MAIL = "mail";
    }

    /**
     * Método responsável pelas requisições em HTTP OPTIONS method.
     * Utilizada em requisições browser (e.g. Chrome, etc).
     */
    @With(AjaxAction.class)
    public static Result options() {
        // A configuração do cabeçalho de response já foi efetuada em AjaxAction.class
        return ok();
    }

}
