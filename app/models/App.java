package models;


import javax.persistence.*;
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
    public int favouriteCount;
    public double rating;
    public int ratingCount;
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
    public String[] screenshots;
    public String level;
    public String area;

    @ManyToOne
    public User submitter;

    @OneToMany
    public Set<Comment> comments;

}
