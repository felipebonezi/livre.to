package models.classes;

import java.util.Date;
import java.util.List;


import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

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

	public String description;

	@Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date created;

	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date modifiedAt;

	@Constraints.Required
	@Lob
	public byte[] materialFile;
	
	@Constraints.Required
	@Lob
	public byte[] materialThumbnail;
	
	@Column(columnDefinition = "integer default 0")
	public Integer score = 0;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "materials")
	private List<User> users;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private Category category;

	@ManyToMany
	@JoinTable(name = "material_comment",
				joinColumns = @JoinColumn(name = "material_id", table = "material_comment"))
	private List<Comment> comments;

	public List<Comment> getComments() {
		return comments;
	}

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

	public byte[] getMaterialThumbnail() {
		return materialThumbnail;
	}

	public void setMaterialThumbnail(byte[] materialThumbnail) {
		this.materialThumbnail = materialThumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void upvote() {
		this.score++;
	}
	
	public void downvote() {
		this.score--;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}
}
