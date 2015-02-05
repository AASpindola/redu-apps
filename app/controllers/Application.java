package controllers;

import models.*;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.*;
import views.html.*;
import services.AppService;
import bootstrap.Constants;

import play.db.jpa.Transactional;

import java.util.*;

public class Application extends Controller {

    public static AppService appService = AppService.getInstance();

    @Transactional
    public static Result index() {

        List<App> apps = appService.getAllApps();
        return ok(index.render(apps, Arrays.asList(Constants.levels)));
    }

}
