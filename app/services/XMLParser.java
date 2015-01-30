package services;

import models.App;
import org.w3c.dom.Document;

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
        app.appName = xmlDoc.getElementsByTagName("name").item(0).getTextContent();
        app.author = xmlDoc.getElementsByTagName("author").item(0).getTextContent();

        return app;
    }

}
