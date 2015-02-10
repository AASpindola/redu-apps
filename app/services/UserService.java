package services;

import controllers.Application;
import controllers.UserSessionController;
import models.User;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;

import javax.persistence.EntityManager;
import static play.mvc.Controller.flash;
import java.util.List;
import static play.mvc.Results.badRequest;

/**
 * Created by arturspindola on 09/02/15.
 */
public class UserService {

    private static UserService instance;
    private static EntityManager em = null;

    public static UserService getInstance() {
        if (instance == null) {
            if (em == null) {
                em = JPA.em("default");
            }
            instance = new UserService();
        }
        return instance;
    }


    public static User getUserByEmail (String email) {

        String q = "SELECT a FROM User a WHERE email = :email";
        List<User> resultSet = JPA.em().createQuery(q, User.class)
                .setParameter("email", email)
                .getResultList();

        return resultSet.get(0);

    }

    public static Result saveUser(User user){

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(user);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });
            flash("success", "You have been registered");
            return UserSessionController.startSession(user.getEmail(), user.getPassword());

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar o usuário: \n" + throwable.getMessage());
        }
    }
}
