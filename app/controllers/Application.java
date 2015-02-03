package controllers;

import models.*;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.*;
import views.html.*;
import services.AppService;
import services.CategoriesService;

import play.db.jpa.Transactional;

import java.util.*;

public class Application extends Controller {

    public static AppService appService = AppService.getInstance();
    public static CategoriesService categoriesService = CategoriesService.getInstance();

    @Transactional
    public static Result index() {

        List<App> apps = appService.getAllApps();
        List<Category> categories = categoriesService.getAllCategories();

        return ok(index.render(apps, categories));
    }

    public static Result newCategory() {
        return ok(views.html.newcategory.render());
    }

    @Transactional
    public static Result saveNewCategory() {

        Map<String, String[]> catQuery_data = request().body().asFormUrlEncoded();
        Category cat = new Category();
        cat.catName = catQuery_data.get("cat-name")[0];
        cat.kind = catQuery_data.get("cat-kind")[0];
        cat.apps = new HashSet<App>();

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(cat);
                    JPA.em().getTransaction().commit();
                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return ok("Categoria " + cat.catName + " adicionada com sucesso!");
    }
}
