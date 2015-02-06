package controllers;

import bootstrap.SE;
import models.User;
import play.data.DynamicForm;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import models.App;
import services.AppService;
import bootstrap.Constants;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import services.CloudinaryService;
import services.ElasticSearchServices;
import views.html.*;

import static play.data.Form.form;

/**
 * Created by arturspindola on 03/02/15.
 */
public class AppController extends Controller {

    public static AppService appService = AppService.getInstance();
    public static ElasticSearchServices elasticSearchServices = ElasticSearchServices.getInstance();

    @Transactional
    public static Result newApp() {
        return ok(newapp.render(Arrays.asList(Constants.levels), Arrays.asList(Constants.area), null));
    }

    @Transactional
    public static Result saveNewApp() {

        // O app já é verificado no frontend

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> appQuery_data = body.asFormUrlEncoded();
        CloudinaryService cloudinaryService = CloudinaryService.getInstance();

        App app = new App();

        app.appName = appQuery_data.get("app-name")[0];
        app.author = appQuery_data.get("app-author")[0];
        app.url = appQuery_data.get("app-url")[0];
        app.language = appQuery_data.get("app-language")[0];
        app.objective = appQuery_data.get("app-objective")[0];
        app.synopsis = appQuery_data.get("app-synopsis")[0];
        app.description = appQuery_data.get("app-description")[0];
        app.publishers = appQuery_data.get("app-publishers")[0];
        app.copyright = appQuery_data.get("app-copyright")[0];
        app.language = appQuery_data.get("app-language")[0];
        app.country = appQuery_data.get("app-country")[0];
        app.level = appQuery_data.get("app-level")[0];
        app.area = appQuery_data.get("app-area")[0];

        app.ratingCount = 0;
        app.rating = 0;
        app.views = 0;
        app.favouriteCount = 0;

        app.comments = new HashSet<>();

        //app.screenshots =
        //app.submitters = pegar o user logado

        Http.MultipartFormData.FilePart thumbnail = body.getFile("app-thumbnail");
        if (thumbnail != null && thumbnail.getFile() != null) {
            try {
                String thumbUrl = cloudinaryService.upload(thumbnail.getFile());
                app.thumbnail = thumbUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        // Indexando no elasticsearch
        SE.client.prepareIndex("reduapps", "app", app.appName)
                .setSource(app.buildXContent())
                .execute()
                .actionGet();

        return ok(app.appName + " adicionado com sucesso!");
    }

    @Transactional
    public static Result register() {

        // O app já é verificado no frontend

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> appQuery_data = body.asFormUrlEncoded();
        CloudinaryService cloudinaryService = CloudinaryService.getInstance();

        User user = new User(appQuery_data.get("user-email")[0], appQuery_data.get("user-login")[0], appQuery_data.get("user-firstname")[0], appQuery_data.get("user-lastname")[0], appQuery_data.get("user-role")[0], appQuery_data.get("user-picture")[0], appQuery_data.get("user-password")[0]);

        Http.MultipartFormData.FilePart thumbnail = body.getFile("app-thumbnail");
        if (thumbnail != null && thumbnail.getFile() != null) {
            try {
                String thumbUrl = cloudinaryService.upload(thumbnail.getFile());
                user.thumbnail = thumbUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(user);
                    JPA.em().getTransaction().commit();
                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // Indexando no elasticsearch
//        SE.client.prepareIndex("reduapps", "user", user.login)
//                .setSource(user.buildXContent())
//                .execute()
//                .actionGet();

        return ok(user.getLogin() + " adicionado com sucesso!");
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

        elasticSearchServices.updateViewsCount(app.appName, app.views);

        return ok(apppage.render(app));
    }

    @Transactional
    public static Result searchAppsByName() {
        DynamicForm params = form().bindFromRequest();
        String query = params.get("q");
        String lastName = params.get("l");
        lastName = lastName == null ? "" : lastName;
        query = query == null ? "" : query;
        //List<App> apps = appService.getAppsByNameLike(query, lastName);
        List<App> apps = elasticSearchServices.searchApps(query);

        return ok(applistcontent.render(apps));
    }
}
