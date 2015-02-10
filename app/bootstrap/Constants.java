package bootstrap;

import play.Play;

/**
 * Created by arturspindola on 05/02/15.
 */
public class Constants {

    public static String [] levels = {"Ensino Infantil", "Ensino Fundamental", "Ensino Médio", "Ensino Superior",
            "Educação Profissional"};

    public static String[] area = {"Ciências Humanas", "Ciências Exatas", "Saúde", "Música", "Artes Plásticas"};

    public static String encryptionKey = Play.application().configuration().getString("encryption.key");
}
