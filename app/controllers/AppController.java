package controllers;

import models.Rating;
import models.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import models.App;
import services.*;
import bootstrap.Constants;

import java.io.IOException;
import java.util.*;

import views.html.*;

import static play.data.Form.form;

/**
 * Created by arturspindola on 03/02/15.
 */
public class AppController extends Controller {

    public static AppService appService = AppService.getInstance();
    public static XMLParser xmlParser = XMLParser.getInstance();
    public static UserService userService = UserService.getInstance();
    public static ElasticSearchServices elasticSearchServices = ElasticSearchServices.getInstance();
    public static RatingService ratingService = RatingService.getInstance();

    @Transactional
    public static Result newApp() {
        User aux = new User();
        try{
            if(ctx().session().get("email")!=null){
                aux = userService.getUserByEmail(ctx().session().get("email"));
            }
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        if (aux.isEmpty()){
            return badRequest("Você precisa estar logado para adicionar um aplicativo");
        } else {
            return ok(newapp.render(Arrays.asList(Constants.levels), Arrays.asList(Constants.area), new App(), aux));
        }
    }

    @Transactional
    public static Result parseXML() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart xmlFile = body.getFile("app-xml");
        Document doc = null;
        try {
            doc = Jsoup.parse(xmlFile.getFile(), "UTF-8", "http://example.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        App app = xmlParser.fromXMLtoApp(doc);

        User aux = new User();
        try{
            if(ctx().session().get("email")!=null){
                aux = userService.getUserByEmail(ctx().session().get("email"));
            }
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return ok(newapp.render(Arrays.asList(Constants.levels), Arrays.asList(Constants.area), app, aux));
    }

    @Transactional
    public static Result saveNewApp() {

        // O app já é verificado no frontend

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> appQuery_data = body.asFormUrlEncoded();

        String appName = appQuery_data.get("app-name")[0];

        if (appService.getAppByName(appName) != null) {
            return badRequest("Aplicativo já existe");
        } else {

            App app = new App();
            app.appName = appName;
            app.author = appQuery_data.get("app-author")[0];
            app.url = appQuery_data.get("app-url")[0];
            app.language = appQuery_data.get("app-language")[0];
            app.objective = appQuery_data.get("app-objective")[0];
            app.synopsis = appQuery_data.get("app-synopsis")[0];
            app.classification = appQuery_data.get("app-classification")[0];
            app.description = appQuery_data.get("app-description")[0];
            app.publishers = appQuery_data.get("app-publishers")[0];
            app.copyright = appQuery_data.get("app-copyright")[0];
            app.language = appQuery_data.get("app-language")[0];
            app.country = appQuery_data.get("app-country")[0];
            app.level = appQuery_data.get("app-level")[0];
            app.area = appQuery_data.get("app-area")[0];

            app.views = 0;

            app.comments = new HashSet<>();

            //app.screenshots =
            //app.submitters = pegar o user logado

            Http.MultipartFormData.FilePart thumbnail = body.getFile("app-thumbnail");
            if (thumbnail != null && thumbnail.getFile() != null) {
                try {
                    CloudinaryService cloudinaryService = CloudinaryService.getInstance();
                    String thumbUrl = cloudinaryService.upload(thumbnail.getFile());
                    app.thumbnail = thumbUrl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Http.MultipartFormData.FilePart screenshot0 = body.getFile("app-screenshot0");
            if (screenshot0 != null && screenshot0.getFile() != null) {
                try {
                    CloudinaryService cloudinaryService = CloudinaryService.getInstance();
                    String ssUrl = cloudinaryService.upload(screenshot0.getFile());
                    app.screenshot0 = ssUrl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Http.MultipartFormData.FilePart screenshot1 = body.getFile("app-screenshot1");
            if (screenshot1 != null && screenshot1.getFile() != null) {
                try {
                    CloudinaryService cloudinaryService = CloudinaryService.getInstance();
                    String ssUrl = cloudinaryService.upload(screenshot1.getFile());
                    app.screenshot1 = ssUrl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return appService.saveNewApp(app);
        }
    }

    @Transactional
    public static Result appPage(String name) {

        App app = appService.getAppByName(name);
        app.views++;
        appService.saveApp(app);

        elasticSearchServices.updateViewsCount(app.appName, app.views);

        User aux = new User();
        double ratingVal = 0;
        try{
            String email = ctx().session().get("email");
            if(email!=null){
                aux = userService.getUserByEmail(email);
                List<Rating> ratings = ratingService.getRating(name, email);
                ratingVal = (ratings==null || ratings.isEmpty()) ? 0 : ratings.get(0).value;
            }
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        double appRatingVal = ratingService.calculateAppRate(name);
        System.out.println(aux.email);
        return ok(apppage.render(app, aux, ratingVal, appRatingVal));
    }

    public static Result searchApps() {
        DynamicForm params = form().bindFromRequest();
        String query = params.get("q");
        String starting = params.get("s");
        String field = params.get("f");
        starting = starting == null ? "0" : starting;
        int startingInt = Integer.parseInt(starting);
        query = query == null ? "" : query;
        field = field == null ? "" : field;
        List<App> apps = elasticSearchServices.searchAppsInRange(field, query, startingInt, startingInt + 12);
        apps = apps == null ? new ArrayList<>() : apps;
        return ok(applistcontent.render(apps));
    }

    public static Result rateApp() {
        DynamicForm params = form().bindFromRequest();
        String ratingValue = params.get("r");
        String userEmail = params.get("u");
        String appName = params.get("a");

        ratingValue = ratingValue == null ? "0" : ratingValue;

        if (userEmail == null || appName == null || userEmail.isEmpty() || appName.isEmpty()){
            return badRequest();
        } else {
            App app = appService.getAppByName(appName);
            ratingService.updateRating(app, userEmail, Double.parseDouble(ratingValue));
            return ok();
        }
    }

}
