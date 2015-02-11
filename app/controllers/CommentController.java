package controllers;

import models.Answer;
import models.Comment;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AppService;
import services.CommentService;
import services.UserService;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Marcel on 08/02/2015.
 */
public class CommentController extends Controller {

    private static CommentService commentService = CommentService.getInstance();
    private static UserService userService = UserService.getInstance();
    private static AppService appService = AppService.getInstance();

    @Transactional
    public static Result saveNewComment() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> commentQuery_data = body.asFormUrlEncoded();

        final Comment comment = new Comment();
        comment.setKind(commentQuery_data.get("kind")[0]);
        comment.setBody(commentQuery_data.get("body")[0]);
        comment.setApp(appService.getAppByName(commentQuery_data.get("app")[0]));
        comment.setAuthor(userService.getUserByEmail(commentQuery_data.get("author")[0]));
        comment.setAnswers(new HashSet<Answer>());
        comment.setPostedTime(new Date());
        return commentService.saveComment(comment);
    }

    @Transactional
    public static Result saveNewAnswer() {

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> commentQuery_data = body.asFormUrlEncoded();

        final Answer answer = new Answer();
        answer.setKind(commentQuery_data.get("kind")[0]);
        answer.setBody(commentQuery_data.get("body")[0]);
        Long commentId = Long.parseLong(commentQuery_data.get("comment")[0]);
        answer.setComment(commentService.getCommentById(commentId));
        answer.setAuthor(userService.getUserByEmail(commentQuery_data.get("author")[0]));
        answer.setPostedTime(new Date());

        return commentService.saveAnswer(answer);
    }
}

