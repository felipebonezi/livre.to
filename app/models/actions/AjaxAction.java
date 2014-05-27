package models.actions;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

/**
 * Action que irá tratar as requisições feitas por browsers (e.g. Chrome)
 * que ao efetuar um POST/OPTIONS solicita as permissões dos cabeçalhos.
 */
public class AjaxAction extends Action.Simple {

    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        Http.Response response = ctx.response();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers","X-Requested-With, Content-Type, X-Auth-Token, Authorization");
        response.setHeader("Access-Control-Expose-Headers","Authorization");

        if (ctx.request().method().toUpperCase().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        return delegate.call(ctx);
    }


}
