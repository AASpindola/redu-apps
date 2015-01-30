package controllers;

import models.App;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.*;

import services.AppService;
import services.CategoriesService;
import views.html.*;

import play.db.jpa.Transactional;

import java.util.Arrays;
import java.util.Map;

public class Application extends Controller {

    @Transactional
    public static Result index() {

        AppService appService = AppService.getInstance();
        App ap = appService.getAppsByAuthor("artur").get(0);

        return ok(index.render(ap.appName));
    }

    @Transactional
    public static Result newApp() {
        CategoriesService categoriesService = CategoriesService.getInstance();
        return ok(views.html.newApp.render(categoriesService.getAllCategories()));
    }

    @Transactional
    public static Result saveNewApp() {

        // O app já é verificado no frontend

        Map<String, String[]> appQuery_data = request().body().asFormUrlEncoded();
        App app = new App();

        app.appName = appQuery_data.get("app-name")[0];
        app.thumbnail = appQuery_data.get("app-thumbnail")[0];
        app.views = Integer.parseInt(appQuery_data.get("app-views")[0]);
        app.url = appQuery_data.get("app-url")[0];
        app.language = appQuery_data.get("app-language")[0];
        app.coreUrl = appQuery_data.get("app-coreUrl")[0];
        app.objective = appQuery_data.get("app-objective")[0];
        app.synopsis = appQuery_data.get("app-synopsis")[0];
        app.description = appQuery_data.get("app-description")[0];
        app.publishers = appQuery_data.get("app-publishers")[0];
        app.submitters = appQuery_data.get("app-submitters")[0];
        app.copyright = appQuery_data.get("app-copyright")[0];

        //Falta users, categories e comments

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(app);
                    JPA.em().getTransaction().commit();
                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return ok(app.appName);
    }

}
