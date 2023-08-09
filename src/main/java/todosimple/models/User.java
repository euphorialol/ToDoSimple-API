package todosimple.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = User.TABLE_NAME)
public class User {

    public interface CreatUser{}
    public interface UpdateUser{}

    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreatUser.class)
    @NotEmpty(groups = CreatUser.class)
    @Size(groups = CreatUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = {CreatUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreatUser.class, UpdateUser.class})
    @Size(groups = {CreatUser.class, UpdateUser.class}, min = 6, max = 60)
    private String password;

    // private List<Task> tasks = new ArrayList<Task>();


    public User() {
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username)
            && Objects.equals(this.password, other.password);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return result;
    }
}
