package controllers;

import models.Answer;
import models.Comment;
import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AppService;
import services.CommentService;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by Marcel on 08/02/2015.
 */
public class CommentController extends Controller {

    @Transactional
    public static Result saveNewComment() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> commentQuery_data = body.asFormUrlEncoded();

        final Comment comment = new Comment();
        comment.setKind(commentQuery_data.get("kind")[0]);
        comment.setBody(commentQuery_data.get("body")[0]);
        comment.setApp(AppService.getAppByName(commentQuery_data.get("app")[0]));
        comment.setAuthor(CommentService.getUserByEmail(commentQuery_data.get("author")[0]));
        comment.setAnswers(new HashSet<Answer>());


        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(comment);
                    JPA.em().getTransaction().commit();
                    return null;
                }
            });

            return ok("Comentario realizado com sucesso!");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return badRequest("Ocorreu um erro ao salvar o comentario: \n" + throwable.getMessage());
        }
    }

    @Transactional
    public static Result saveNewAnswer() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> commentQuery_data = body.asFormUrlEncoded();

        final Answer answer = new Answer();
        answer.setKind(commentQuery_data.get("kind")[0]);
        answer.setBody(commentQuery_data.get("body")[0]);
        answer.setComment(CommentService.getCommentById(Long.getLong(commentQuery_data.get("comment")[0])));
        answer.setAuthor(CommentService.getUserByEmail(commentQuery_data.get("author")[0]));


        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(answer);
                    JPA.em().getTransaction().commit();
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

