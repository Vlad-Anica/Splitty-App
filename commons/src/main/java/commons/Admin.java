package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Admin extends User {
    private String password;

    public Admin() {

    }

    /**
     * The constructor that sets the id and password of an Admin
     * @param id the id
     * @param password the password
     */
    public Admin(long id, String password) {
        super(id);
        this.password = password;
    }

    /**
     * gets the password
     * @return returns the password as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the password
     * @param password the password as a String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Admin admin = (Admin) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(password, admin.password).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(password).toHashCode();
    }
}
