package models.classes;

import java.io.File;

import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
public class Material extends Model {

    public enum PricePolicy {
	FREE, MINIMUM_VALUE, FIXED_VALUE
    }

    public User author;
    public String title;
    public PricePolicy pricePolicy;
    public String price;
    public File materialFile;
    
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public PricePolicy getPricePolicy() {
        return pricePolicy;
    }
    public void setPricePolicy(PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public File getMaterialFile() {
        return materialFile;
    }
    public void setMaterialFile(File materialFile) {
        this.materialFile = materialFile;
    }

}
