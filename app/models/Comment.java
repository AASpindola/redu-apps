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
    public long commentId;
    @Lob
    public String body;
    public String kind;
    @ManyToOne
    public User author;
    @ManyToOne
    public App app;
    @OneToMany
    public Set<Answer> answers;
}
