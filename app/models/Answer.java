package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by arturspindola on 30/01/15.
 */


@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long answerId;
    private Date postedTime;
    @Lob
    private String body;
    private String kind;
    @ManyToOne
    private User author;
    @ManyToOne
    private Comment comment;

    public long getAnswerId() {
        return answerId;
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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Date getPostedTime(){
        return postedTime;
    }

    public void setPostedTime(Date postedTime){
        this.postedTime = postedTime;
    }
}
