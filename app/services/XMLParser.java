package services;

import models.App;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tyrex.util.ArraySet;

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

        app.appName = xmlDoc.getElementsByTagName("title").item(0).getTextContent();

        //app.thumbnail;
        app.views = 0;
        app.url = xmlDoc.getElementsByTagName("lom").item(0).getAttributes().getNamedItem("xmlns:lom").getTextContent();
        app.language = xmlDoc.getElementsByTagName("language").item(0).getTextContent();
        //app.objective;
        //app.synopsis;
        app.description = xmlDoc.getElementsByTagName("descriptionUnbounded").item(0).getTextContent();

        app.author = "";
        app.publishers = "";
        NodeList contributes = xmlDoc.getElementsByTagName("contribute");
        for (int i = 0; i < contributes.getLength(); i++) {

            Node node = contributes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element contribute = (Element) node;
                String contValue = contribute.getElementsByTagName("value").item(0).getTextContent();
                String contEntity = contribute.getElementsByTagName("entity").item(0).getTextContent();

                if (contValue.equals("author")) {
                    app.author += contEntity;
                } else {
                    if (contValue.equals("publisher")) {
                        app.publishers += contEntity;
                    }
                }
            }
        }

        //app.copyright;
        app.country = xmlDoc.getElementsByTagName("location").item(0).getTextContent();

//        app.categories = new ArraySet();
//
//        app.users;
//
//        app.comments = new ArraySet();


        return app;
    }

    public static Document fromApptoXML (App app) {
        return null;
    }

}
