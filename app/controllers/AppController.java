package controllers;

import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import models.App;
import services.AppService;
import services.CategoriesService;

import views.html.*;

import java.util.List;
import java.util.Map;

import static play.data.Form.form;

/**
 * Created by arturspindola on 03/02/15.
 */
public class AppController extends Controller {

    public static AppService appService = AppService.getInstance();
    public static CategoriesService categoriesService = CategoriesService.getInstance();

    @Transactional
    public static Result newApp() {
        return ok(views.html.newapp.render(categoriesService.getAllCategories()));
    }

    @Transactional
    public static Result saveNewApp() {

        // O app já é verificado no frontend

        Map<String, String[]> appQuery_data = request().body().asFormUrlEncoded();
        App app = new App();

        app.appName = appQuery_data.get("app-name")[0];
        app.thumbnail = appQuery_data.get("app-thumbnail")[0];
        app.views = 0;
        app.url = appQuery_data.get("app-url")[0];
        app.language = appQuery_data.get("app-language")[0];
        app.coreUrl = appQuery_data.get("app-coreUrl")[0];
        app.objective = appQuery_data.get("app-objective")[0];
        app.synopsis = appQuery_data.get("app-synopsis")[0];
        app.description = appQuery_data.get("app-description")[0];
        app.publishers = appQuery_data.get("app-publishers")[0];
        app.submitters = appQuery_data.get("app-submitters")[0];
        app.copyright = appQuery_data.get("app-copyright")[0];

        //Falta users, author, categories e comments

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

        return ok(app.appName + " adicionado com sucesso!");
    }

    @Transactional
    public static Result appPage(String name) {
        App app = appService.getAppByName(name);
        app.views++;

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

        return ok(views.html.apppage.render(app));
    }

    @Transactional
    public static Result searchAppsByName() {
        DynamicForm params = form().bindFromRequest();
        String query = params.get("q");
        String lastName = params.get("l");
        lastName = lastName == null ? "" : lastName;
        query = query == null ? "" : query;
        List<App> apps = appService.getAppsByNameLike(query, lastName);
        return ok(views.html.applistcontent.render(apps));
    }

    @Transactional
    public static Result searchAppsByCategory() {
        return ok();
    }
}
