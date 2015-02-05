package services;

import com.cloudinary.Cloudinary;
import play.Logger;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by arturspindola on 05/02/15.
 */
public class CloudinaryService {

    private static CloudinaryService instance;
    private Cloudinary cloudinary;

    private CloudinaryService() {
        this.init();
    }

    private void init() {
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

        if (cloudinaryUrl == null) {
            String cloudName = Play.application().configuration().getString("cloudinary.cloudname");
            String apiKey = Play.application().configuration().getString("cloudinary.api.key");
            String apiSecret = Play.application().configuration().getString("cloudinary.api.secret");
            cloudinaryUrl = "cloudinary://" + apiKey + ":" + apiSecret + "@" + cloudName;
        } else {
            Logger.debug("[cloudinary] auto configuration found");
        }

        cloudinary = new Cloudinary(cloudinaryUrl);
        Logger.debug("[cloudinary] loaded");
    }

    public static synchronized CloudinaryService getInstance() {
        return (instance == null ? new CloudinaryService() : instance);
    }

    public String upload(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, Cloudinary.emptyMap());
        return uploadResult.get("url").toString().replaceAll("http:","");
    }

}

