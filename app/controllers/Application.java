package controllers;

import play.mvc.*;
import views.html.*;
import bootstrap.Constants;

import java.util.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Arrays.asList(Constants.levels)));
    }

}
