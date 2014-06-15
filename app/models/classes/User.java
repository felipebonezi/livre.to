package models.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.format.Formats;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by felipebonezi on 27/05/14.
 */
@Entity
@SequenceGenerator(name = User.SEQUENCE_NAME, sequenceName = User.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class User extends Model {

    private static final long serialVersionUID = 1L;
    public static final String SEQUENCE_NAME = "user_id_seq";

    public enum Status {
	ACTIVE, REMOVED;
    }

    public enum Gender {
	FEMALE, MALE;
    }
    
    public enum Group {
	ADMINISTRATOR, AUTHOR, CONSUMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private long id;

    private String accessToken;
    private String login;
    private String password;
    private String name;
    private String mail;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Group> groups;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    @Version
    private Date modifiedAt;

    @ManyToMany
    @JoinTable(name = "user_has_material",
            joinColumns = @JoinColumn(name = "user_id", table = "user_has_material"),
    inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<Material> materials;

    public List<Material> getMaterials() {
        return materials;
    }

    public User() {
//	FIXME achar um jeito de integrar com DB
	this.groups = new ArrayList<Group>(Arrays.asList(Group.AUTHOR, Group.CONSUMER));
    }

    @JsonIgnore
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @JsonIgnore
    public String getAccessToken() {
	return accessToken;
    }

    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    @JsonIgnore
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    @JsonIgnore
    public Date getModifiedAt() {
	return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
	this.modifiedAt = modifiedAt;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    
    public void addGroup(Group group) {
	this.groups.add(group);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
