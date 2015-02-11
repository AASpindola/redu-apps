package controllers;

import bootstrap.SE;
import models.User;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import services.UserService;
import views.html.*;
import bootstrap.Constants;

import java.util.*;

public class Application extends Controller {

    public static UserService userService = UserService.getInstance();

    @Transactional
    public static Result index() {
        Logger.debug(ctx().session().get("email"));
        User aux = new User();
        try{
            if(ctx().session().get("email")!=null){
                aux = userService.getUserByEmail(ctx().session().get("email"));
            }
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return ok(index.render(Arrays.asList(Constants.levels), aux));
    }

    public static Result deleteSE() {
        SE.client.admin().indices().prepareDelete("reduapps").execute()
                .actionGet();
        return ok("deleted!");
    }

}
