package models;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by arturspindola on 30/01/15.
 */

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long commentId;
    @Lob
    private String body;
    private String kind;
    @ManyToOne
    private User author;
    @ManyToOne
    private App app;
    @OneToMany
    private Set<Answer> answers;


    public long getCommentId() {
        return commentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
