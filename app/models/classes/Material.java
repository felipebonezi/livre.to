package models.classes;

import java.io.File;
import java.util.Date;

import javax.persistence.*;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.EnumMapping;

@Entity
@SequenceGenerator(name = Material.SEQUENCE_NAME, sequenceName = Material.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Material extends Model {
    private static final long serialVersionUID = 1L;
    public static final String SEQUENCE_NAME = "material_seq";

    @EnumMapping(nameValuePairs = "FREE=F, MINIMUM_VALUE=M, FIXED_VALUE=V")
    public enum PricePolicy {
	FREE, MINIMUM_VALUE, FIXED_VALUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    public long id;

    @ManyToOne
    public User author;

    @Constraints.Required
    public String title;

    @Constraints.Required
    @Enumerated(value = EnumType.STRING)
    public PricePolicy pricePolicy;

    public String price;

    @Constraints.Required
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date created;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date modifiedAt;

    @Constraints.Required
    @Lob
    public byte[] materialFile;

    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long, Material> find = new Finder<Long, Material>(
	    Long.class, Material.class);
    
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

    public byte[] getMaterialFile() {
	return materialFile;
    }

    public void setMaterialFile(byte[] materialFile) {
	this.materialFile = materialFile;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public long getId() {
        return id;
    }
}
