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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="app_lecture", joinColumns = {@JoinColumn(name="appName")}, inverseJoinColumns = {@JoinColumn(name="lecId")})
    public Set<Lecture> lectures;

}
