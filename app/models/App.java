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
    public String url;
    public String author;
    public String language;
    public String coreUrl;
    @Lob
    public String objective;
    @Lob
    public String synopsis;
    @Lob
    public String description;
    public String publishers;
    public String submitters;
    public String copyright;
    public String country;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="app_category", joinColumns = {@JoinColumn(name="appName")}, inverseJoinColumns = {@JoinColumn(name="catName")})
    public Set<Category> categories;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="app_user", joinColumns = {@JoinColumn(name="appName")}, inverseJoinColumns = {@JoinColumn(name="email")})
    public Set<User> users;

    @OneToMany
    public Set<Comment> comments;

}
