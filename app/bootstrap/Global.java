package bootstrap;

import play.Application;
import play.GlobalSettings;
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
