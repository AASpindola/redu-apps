package models;

/**
 * Created by arturspindola on 10/02/15.
 */

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long ratingId;
    public String email;
    public double value;
    @ManyToOne
    public App app;

    public Rating (String email, double value){
        this.email = email;
        this.value = value;
    }
}
