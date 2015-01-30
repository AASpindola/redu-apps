package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Created by arturspindola on 30/01/15.
 */

@Entity
public class Category {

    @Id
    public String catName;
    public String kind;

    @ManyToMany(mappedBy = "categories")
    public Set<App> apps;
}
