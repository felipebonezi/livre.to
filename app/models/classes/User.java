package models.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import play.data.format.*;

/**
 * Created by felipebonezi on 27/05/14.
 */
@Entity
public class User extends Model {

    private static final long serialVersionUID = 1L;

    public enum Status {
	ACTIVE, BLOCKED;
    }

    public enum Gender {
	FEMALE, MALE;
    }
    
    public enum Group {
	ADMINISTRATOR, AUTHOR, CONSUMER
    }

    @Id
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
    private Date modifiedAt;
    
    public User() {
//	FIXME achar um jeito de integrar com DB
//	this.groups = new ArrayList<Group>(Arrays.asList(Group.AUTHOR, Group.COSUMER));
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
