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
        if(User.authenticate(appQuery_data.get("login")[0],  appQuery_data.get("password")[0])==null){
            return badRequest();
        }
        session().clear();
        session("email", appQuery_data.get("login")[0]);
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
}