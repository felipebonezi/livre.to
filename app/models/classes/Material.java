package models.classes;

import java.io.File;
import java.sql.Date;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.EnumMapping;

import play.data.format.*;

@Entity
public class Material extends Model
{
	private static final long serialVersionUID = 1L;

	@EnumMapping(nameValuePairs = "FREE=F, MINIMUM_VALUE=M, FIXED_VALUE=F")
	public enum PricePolicy
	{
		FREE, MINIMUM_VALUE, FIXED_VALUE
	}
	
	@Id
	public Long id;

	@ManyToOne
	public User author;

	@Constraints.Required
	public String title;

	@Constraints.Required
	@ElementCollection
    @Enumerated(EnumType.STRING)
	public PricePolicy pricePolicy;
	
	public String price;
	
	@Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-dd")
    public Date created;
	
	@Formats.DateTime(pattern="yyyy-MM-dd")
    public Date lastModified;

	@Constraints.Required
	public File materialFile;

	public User getAuthor()
	{
		return author;
	}

	public void setAuthor(User author)
	{
		this.author = author;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public PricePolicy getPricePolicy()
	{
		return pricePolicy;
	}

	public void setPricePolicy(PricePolicy pricePolicy)
	{
		this.pricePolicy = pricePolicy;
	}

	public String getPrice()
	{
		return price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public File getMaterialFile()
	{
		return materialFile;
	}

	public void setMaterialFile(File materialFile)
	{
		this.materialFile = materialFile;
	}
}
