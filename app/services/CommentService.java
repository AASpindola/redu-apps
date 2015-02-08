package services;

import models.App;
import models.Comment;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by Marcel on 08/02/2015.
 */
public class CommentService {

    private static CommentService instance;

    public static CommentService getInstance() {
        if (instance == null) {
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

}
