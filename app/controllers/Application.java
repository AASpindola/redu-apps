package controllers;

import models.App;
import play.mvc.*;

import services.AppService;
import views.html.*;

import play.db.jpa.Transactional;

public class Application extends Controller {

    @Transactional
    public static Result index() {

        AppService appService = AppService.getInstance();
        App ap = appService.getAppsByAuthor("artur").get(0);

        return ok(index.render(ap.appName));
    }

}
