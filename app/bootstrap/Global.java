package bootstrap;

import models.App;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;

/**
 * Created by arturspindola on 21/01/15.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        App app = new App();
        app.appName = "meu primeiro app";
        app.author = "artur";

        try {
            JPA.withTransaction(new F.Function0<Boolean>() {
                @Override
                public Boolean apply() throws Throwable {
                    JPA.em().persist(app);
                    JPA.em().getTransaction().commit();
                    return null;
                }
            });
        } catch (Throwable throwable) {
            Logger.debug("entrada duplicada");
        }
    }
}
