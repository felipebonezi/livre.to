package models.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by felipebonezi on 27/05/14.
 */
@Entity
@Table(name = "user")
public class User extends Model {

    public enum Status {
        ACTIVE, BLOCKED;
    }

    @Id
    private long id;

    private String accessToken;
    private String login;
    private String password;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Version
    private Calendar modifiedAt;

    @JsonIgnore
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    @JsonIgnore
    public Calendar getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Calendar modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
