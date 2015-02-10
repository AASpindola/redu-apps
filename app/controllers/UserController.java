package controllers;

import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CloudinaryService;
import services.UserService;

import java.io.IOException;
import java.util.Map;
import views.html.*;


/**
 * Created by arturspindola on 09/02/15.
 */
public class UserController extends Controller {

    public static UserService userService = UserService.getInstance();

    public static Result newUser() {
        return ok(newuser.render());
    }

    public static Result saveNewUser() {

        // O app já é verificado no frontend

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Map<String, String[]> appQuery_data = body.asFormUrlEncoded();
        CloudinaryService cloudinaryService = CloudinaryService.getInstance();

        User user = new User(appQuery_data.get("user-email")[0], appQuery_data.get("user-firstname")[0], appQuery_data.get("user-lastname")[0], appQuery_data.get("user-role")[0], appQuery_data.get("user-password")[0]);

        Http.MultipartFormData.FilePart thumbnail = body.getFile("app-thumbnail");
        if (thumbnail != null && thumbnail.getFile() != null) {
            try {
                String thumbUrl = cloudinaryService.upload(thumbnail.getFile());
                user.thumbnail = thumbUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userService.saveUser(user);
    }
}