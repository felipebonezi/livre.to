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
@SequenceGenerator(name = Comment.SEQUENCE_NAME, sequenceName = Comment.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Comment extends Model {
	
	private static final long serialVersionUID = 1L;
	public static Finder<Long, Comment> find = new Finder<Long, Comment>(Long.class, Comment.class);
	public static final String SEQUENCE_NAME = "comment_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
	public long id;

	@ManyToOne
	public User author;

	@Constraints.Required
	public String content;

	@Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date created;

	public long getId() {
	    return id;
	}

	public void setId(long id) {
	    this.id = id;
	}

	public User getAuthor() {
	    return author;
	}

	public void setAuthor(User author) {
	    this.author = author;
	}

	public String getContent() {
	    return content;
	}

	public void setContent(String content) {
	    this.content = content;
	}

	public Date getCreated() {
	    return created;
	}

	public void setCreated(Date created) {
	    this.created = created;
	}

}
