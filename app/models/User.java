package models;

import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.*;
import play.db.jpa.Transactional;
import services.UserService;
import utils.Cryptor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arturspindola on 30/01/15.
 */

@Entity
public class User {

    @Id
    public String email;
    public String firstName;
    public String lastName;
    public String role;
    public String thumbnail;

    private String password;
    private boolean empty;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    public Set<Comment> comments;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    public Set<Answer> answers;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submitter")
    public Set<App> apps;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isEmpty() {
        if(email==null||firstName==null||lastName==null){
            return true;
        }
        return false;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public User() {
        comments = new HashSet<Comment>();
        answers = new HashSet<Answer>();
        apps = new HashSet<App>();
        setEmpty(true);
    }

    public User(String email, String firstName, String lastName, String role, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = Cryptor.encrypt(password);
        comments = new HashSet<Comment>();
        answers = new HashSet<Answer>();
        apps = new HashSet<App>();
        setEmpty(false);
    }

    @Transactional
    public static User authenticate(String email, String password){
        UserService userService = UserService.getInstance();
        User aux =  userService.getUserByEmail(email);
        if(aux.getPassword().equals(Cryptor.encrypt(password))){
            return aux;
        }else{
            return null;
        }
    }
}
