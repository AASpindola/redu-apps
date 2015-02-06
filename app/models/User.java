package models;

import javax.persistence.*;
import java.util.HashSet;
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
    private String password;

    @OneToMany
    public Set<Comment> comments;
    @OneToMany
    public Set<Answer> answers;
    @OneToMany
    public Set<App> apps;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<App> getApps() {
        return apps;
    }

    public void setApps(Set<App> apps) {
        this.apps = apps;
    }

    public User() {
    }

    public User(String email, String login, String firstName, String lastName, String role, String thumbnail, String password) {
        this.email = email;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.thumbnail = thumbnail;
        this.password = password;
        comments = new HashSet<Comment>();
        answers = new HashSet<Answer>();
        apps = new HashSet<App>();
    }
}
