package controllers;

import models.User;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;
import views.html.*;
import bootstrap.Constants;

import java.util.*;

public class Application extends Controller {

    @Transactional
    public static Result index() {
        Logger.debug(ctx().session().get("email"));
        User aux = new User();
        try{
            aux = JPA.em().find(User.class, ctx().session().get("email"));
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        Logger.debug(ctx().session().get("email"));
        return ok(index.render(Arrays.asList(Constants.levels), aux));
    }

}
