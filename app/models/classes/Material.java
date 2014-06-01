package models.classes;

import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
public class Material extends Model {
    
    public String name;
    public User author;
    public float price;
    
}
