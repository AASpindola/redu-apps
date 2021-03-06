package models;


import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import javax.persistence.*;
import java.io.IOException;
import java.util.Set;

/**
 * Created by arturspindola on 29/01/15.
 */

@Entity
public class App {

    @Id
    public String appName;
    public String thumbnail;
    public int views;
    public String url;
    public String author;
    public String language;
    @Lob
    public String objective;
    @Lob
    public String synopsis;
    @Lob
    public String description;
    public String publishers;
    public String copyright;
    public String country;
    public String screenshot0;
    public String screenshot1;
    public String classification;
    public String level;
    public String area;
    @OneToMany(mappedBy = "app")
    public Set<Rating> rateMapping;

    @ManyToOne
    public User submitter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="app")
    public Set<Comment> comments;
    public int commentsCount;

    public XContentBuilder buildXContent (){
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder().startObject();
            builder.field("appName", appName);
            builder.field("author", author);
            builder.field("objective", objective);
            builder.field("synopsis", synopsis);
            builder.field("description", description);
            builder.field("thumbnail", thumbnail);
            builder.field("language", language);
            builder.field("views", views);
            builder.field("commentsCount", commentsCount);
            builder.field("level", level);
            builder.field("area", area);
            builder.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public void buildFromJson(JsonNode json){
        appName = json.get("appName") == null ? "" : json.get("appName").asText();
        author = json.get("author") == null ? "" : json.get("author").asText();
        thumbnail = json.get("thumbnail") == null ? null : json.get("thumbnail").asText();
        language = json.get("language") == null ? "" : json.get("language").asText();
        views = json.get("views") == null ? 0 : json.get("views").asInt();
        commentsCount = json.get("commentsCount") == null ? 0 : json.get("commentsCount").asInt();
        level = json.get("level") == null ? "" : json.get("level").asText();
        area = json.get("area") == null ? "" : json.get("area").asText();
    }

}
