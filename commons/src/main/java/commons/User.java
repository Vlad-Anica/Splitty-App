package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    /**
     * Constructor for User
     */
    public User(long id) {
        this.id = id;
    }

    /**
     * Constructor without arguments
     */
    public User() {

    }

    /**
     * gets the id
     *
     * @return returns the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id you want it to have
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().append(id, user.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
