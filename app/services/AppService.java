package services;

import models.App;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by arturspindola on 29/01/15.
 */
public class AppService {

    private static AppService instance;

    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    public static App getAppById (int id) {

        return JPA.em().find(App.class, id);
    }

    public static List<App> getAppsByAuthor (String author) {

        String q = "SELECT a FROM App a WHERE author = :author";
        List<App> resultSet = JPA.em().createQuery(q, App.class)
                .setParameter("author", author)
                .getResultList();

        return resultSet;
    }

}
