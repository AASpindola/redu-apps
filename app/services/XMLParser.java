package services;

import models.App;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by arturspindola on 29/01/15.
 */
public class XMLParser {

    private static XMLParser instance;

    public static XMLParser getInstance() {
        if (instance == null) {
            instance = new XMLParser();
        }
        return instance;
    }

    public static App fromXMLtoApp (Document xmlDoc) {

        App app = new App();

        app.appName = xmlDoc.getElementsByTag("title").get(0).text();
        app.views = 0;
        app.url = xmlDoc.getElementsByTag("lom").get(0).attr("xmlns:lom");
        app.language = xmlDoc.getElementsByTag("language").get(0).text();
        app.description = xmlDoc.getElementsByTag("descriptionUnbounded").get(0).text();

        app.author = "";
        app.publishers = "";
        Elements contributes = xmlDoc.getElementsByTag("contribute");
        for (Element contribute : contributes) {
            String contValue = contribute.getElementsByTag("value").get(0).text();
            String contEntity = contribute.getElementsByTag("entity").get(0).text();
            if (contValue.equals("author")) {
                app.author += contEntity + ", ";
            } else {
                if (contValue.equals("publisher")) {
                    app.publishers += contEntity + ", ";
                }
            }
        }

        app.copyright = "";
        Elements copyrights = xmlDoc.getElementsByTag("copyrightAndOtherRestrictions");
        for (Element copyright : copyrights){
            app.copyright += copyright.text() + "; ";
        }
        app.country = xmlDoc.getElementsByTag("location").get(0).text();

        return app;
    }

    public static Document fromApptoXML (App app) {
        return null;
    }

}
