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
@SequenceGenerator(name = Category.SEQUENCE_NAME, sequenceName = Category.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Category extends Model {
	
	private static final long serialVersionUID = 1L;
	public static Finder<Long, Category> find = new Finder<Long, Category>(Long.class, Category.class);
	public static final String SEQUENCE_NAME = "category_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
	public long id;

	@Constraints.Required
	public String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
