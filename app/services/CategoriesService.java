package services;

import models.Category;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by arturspindola on 30/01/15.
 */
public class CategoriesService {

    private static CategoriesService instance;

    public static CategoriesService getInstance() {
        if (instance == null) {
            instance = new CategoriesService();
        }
        return instance;
    }

    public static Category getCategoryByName (String name) {
        return JPA.em().find(Category.class, name);
    }

    public static List<Category> getAllCategories() {
        String q = "SELECT a FROM Category a";
        List<Category> resultSet = JPA.em().createQuery(q, Category.class)
                .getResultList();

        return resultSet;
    }
}
