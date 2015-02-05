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
        SE.init();
    }
}
