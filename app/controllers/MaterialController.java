package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.material.*;

public class MaterialController extends Controller {

    public static Result create() {
	return ok(creatematerial.render(""));
    }
}
