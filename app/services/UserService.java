package services;

import bootstrap.SE;
import models.User;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;

import javax.persistence.EntityManager;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

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

            return ok(user.email + " adicionado com sucesso!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar o usuário: \n" + throwable.getMessage());
        }
    }
}
