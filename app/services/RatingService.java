package services;

import bootstrap.SE;
import models.App;
import models.Rating;
import play.db.jpa.JPA;
import play.libs.F;

import javax.persistence.EntityManager;
import java.util.List;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by arturspindola on 10/02/15.
 */
public class RatingService {

    private static RatingService instance;
    private static EntityManager em = null;

    public static RatingService getInstance() {
        if (instance == null) {
            if (em == null) {
                em = JPA.em("default");
            }
            instance = new RatingService();
        }
        return instance;
    }

    public static List<Rating> getRating(String appName, String email){
        String q = "SELECT a FROM Rating a WHERE app.appName = :appName AND email =:userEmail";
        List<Rating> resultSet = em.createQuery(q, Rating.class)
                .setParameter("appName", appName)
                .setParameter("userEmail", email)
                .getResultList();
        return resultSet;
    }

    public static void saveRating (Rating rating){
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(rating);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void updateRating (App app, String userEmail, double ratingValue){

        List<Rating> resultSet = getRating(app.appName, userEmail);
        Rating rating = null;
        if (resultSet==null || resultSet.isEmpty()){
            rating = new Rating(userEmail, ratingValue, app);
            saveRating(rating);
        } else {
            if (resultSet.size() == 1){
                Rating rat = resultSet.get(0);
                rat.value = ratingValue;
                saveRating(rat);
            }
        }
    }

}
