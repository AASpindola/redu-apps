package models;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by arturspindola on 30/01/15.
 */

@Entity
public class User {

    @Id
    public String email;
    @Column(unique = true)
    public String login;
    public String firstName;
    public String lastName;
    public String role;
    public String thumbnail;

    @OneToMany
    public Set<Comment> comments;
    @OneToMany
    public Set<Answer> answers;
    @OneToMany
    public Set<App> apps;
}
