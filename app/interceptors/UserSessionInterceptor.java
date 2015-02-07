package interceptors;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.SimpleResult;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import services.UserService;
import util.UserSession;

/**
 * This interceptor checks the status of the UserSession. Important in debugging session related errors
 * */
public class UserSessionInterceptor extends Action.Simple{

    /** singleton instance of {@link UserService}  */
    private static final UserService userService = UserService.getInstance();

    /**
     * This method is run before executing the context action.
     * @param ctx
     * @return null
     * */
    public Promise<SimpleResult> before(final Context ctx){
        Session session = ctx.session();
        UserSession userSession = userService.getUserSession(session);

        Logger.debug("UserSessionInterceptor.before: USER SESSION UUID - "+ userSession.getUUID() + "\n");
        return null;
    }
    /**
     * After executing the context action this method is called.
     * @param ctx
     * @return null
     * */
    public Promise<SimpleResult> after(final Context ctx){
        // print debug after action finishes
        return null;
    }

    @Override
    public Promise<SimpleResult> call(final Context ctx) throws Throwable {
        Promise<SimpleResult> before = null, result = null, after = null;
        // execute this method before the action is run

        before = before(ctx);

        if(before != null){
            return before;
        }

        result = delegate.call(ctx);

        after = after(ctx);

        if(after != null){
            return after;
        }

        return result;
    }
}
