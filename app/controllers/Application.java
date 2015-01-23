package controllers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Messages.get("text.language")));
    }

}
