package services;

import models.Answer;
import models.App;
import models.Comment;
import models.User;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Result;

import javax.persistence.EntityManager;
import java.util.List;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 * Created by Marcel on 08/02/2015.
 */
public class CommentService {

    private static CommentService instance;
    private static EntityManager em = null;


    public static CommentService getInstance() {
        if (instance == null) {
            if (em == null) {
                em = JPA.em("default");
            }
            instance = new CommentService();
        }
        return instance;
    }

    public static Comment getCommentById (long id) {

        return JPA.em().find(Comment.class, id);
    }

    public static List<Comment> getCommentsFromApp (String appName) {


       final App app = JPA.em().find(App.class, appName);

        String q = "SELECT a FROM Comment a WHERE app = :app";
        List<Comment> resultSet = JPA.em().createQuery(q, Comment.class)
                .setParameter("app", app)
                .getResultList();

        return resultSet;
    }

    public Result saveComment (Comment comment) {

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(comment);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });

            return ok("Comentário adicionado com sucesso!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar o comentário: \n" + throwable.getMessage());
        }
    }

    public Result saveAnswer (Answer answer){

        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    em.persist(answer);
                    em.flush();
                    em.getTransaction().commit();
                    return null;
                }
            });

            return ok("Resposta adicionada com sucesso!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar a resposta: \n" + throwable.getMessage());
        }
    }
}
