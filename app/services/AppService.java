package services;

import bootstrap.SE;
import models.App;
import models.Rating;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by arturspindola on 29/01/15.
 */
public class AppService {

    private static AppService instance;
    private static EntityManager em = null;

    public static AppService getInstance() {
        if (instance == null) {
            if (em == null) {
                em = JPA.em("default");
            }
            instance = new AppService();
        }
        return instance;
    }

    public static App getAppByName (String name) {

        return em.find(App.class, name);
    }

    public static List<App> getAllApps () {
        String q = "SELECT a FROM App a";
        List<App> resultSet = em.createQuery(q, App.class)
                .getResultList();
        return resultSet;
    }

    public static List<App> getAppsByAuthor (String author) {

        String q = "SELECT a FROM App a WHERE author = :author";
        List<App> resultSet = em.createQuery(q, App.class)
                .setParameter("author", author)
                .getResultList();

        return resultSet;
    }

    public static List<App> getAppsByNameLike (String name, String lastName){

        if (lastName.equals("")) {
            String q = "SELECT a FROM App a WHERE appName like :name order by appName asc";
            List<App> resultSet = em.createQuery(q, App.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
            return resultSet.size() >12 ? resultSet.subList(0, 12) : resultSet;
        } else {
            String q = "SELECT a FROM App a WHERE appName like :name and appName > :lastName order by appName asc";
            List<App> resultSet = em.createQuery(q, App.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("lastName", lastName)
                    .getResultList();
            return resultSet.size() >12 ? resultSet.subList(0, 12) : resultSet;
        }
    }

    public static Result saveNewApp (App app){

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(app);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });

            // Indexando no elasticsearch
            SE.client.prepareIndex("reduapps", "app", app.appName)
                    .setSource(app.buildXContent())
                    .execute()
                    .actionGet();

            return ok(app.appName + " adicionado com sucesso!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar o app: \n" + throwable.getMessage());
        }
    }

    public static void saveApp (App app) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(app);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void saveRatingMap(String appName, double rating, String userEmail){
        App app = getAppByName(appName);
        if (app.rateMapping == null){
            app.rateMapping = new HashSet<>();
        }
        app.rateMapping.add(new Rating(userEmail, rating));
        saveApp(app);
    }

}
