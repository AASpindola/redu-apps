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

    public static App getAppByName (String name) {

        return JPA.em().find(App.class, name);
    }

    public static List<App> getAllApps () {
        String q = "SELECT a FROM App a";
        List<App> resultSet = JPA.em().createQuery(q, App.class)
                .getResultList();
        return resultSet;
    }

    public static List<App> getAppsByAuthor (String author) {

        String q = "SELECT a FROM App a WHERE author = :author";
        List<App> resultSet = JPA.em().createQuery(q, App.class)
                .setParameter("author", author)
                .getResultList();

        return resultSet;
    }

    public static List<App> getAppsByNameLike (String name, String lastName){

        if (lastName.equals("")) {
            String q = "SELECT a FROM App a WHERE appName like :name order by appName asc";
            List<App> resultSet = JPA.em().createQuery(q, App.class)
                    .setParameter("name", "%" + name + "%")
                    .getResultList();
            return resultSet.size() >12 ? resultSet.subList(0, 12) : resultSet;
        } else {
            String q = "SELECT a FROM App a WHERE appName like :name and appName > :lastName order by appName asc";
            List<App> resultSet = JPA.em().createQuery(q, App.class)
                    .setParameter("name", "%" + name + "%")
                    .setParameter("lastName", lastName)
                    .getResultList();
            return resultSet.size() >12 ? resultSet.subList(0, 12) : resultSet;
        }
    }

}
