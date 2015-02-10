package controllers;

import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

import java.util.Map;


/**
 * Created by pedro on 07/02/15.
 */
public class  UserSessionController extends Controller {

    public static Result login(){
        return ok(login.render(Form.form(Login.class)));
    }

    @Transactional
    public static Result authenticate(){
        Map<String, String[]> appQuery_data = request().body().asFormUrlEncoded();
        return startSession(appQuery_data.get("login")[0], appQuery_data.get("password")[0]);
    }

    @Transactional
    public static Result startSession(String login, String password){
        if(User.authenticate(login, password)==null){
            return badRequest("Erro");
        }
        session().clear();
        session("email", login);
        return Application.index();
    }

    @Transactional
    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    @Transactional
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return Application.index();
    }
}
