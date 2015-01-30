package models;

import javax.persistence.*;

/**
 * Created by arturspindola on 30/01/15.
 */


@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long answerId;
    @Lob
    public String body;
    public String kind;
    @ManyToOne
    public User author;
    @ManyToOne
    public Comment comment;
}
