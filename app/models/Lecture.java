package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Created by arturspindola on 29/01/15.
 */

@Entity
public class Lecture {

    @Id
    public long lecId;
    public String lecType;
    public String lid;
    public String name;

    @ManyToMany (mappedBy = "lectures")
    public Set<App> apps;
}
