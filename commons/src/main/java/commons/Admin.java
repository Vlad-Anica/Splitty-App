package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Admin extends User {
    private String password;

    public Admin() {

    }

    public Admin(String firstName, String lastName, String IBAN, String BIC, String email, Currency preferredCurrency, String password) {
        super(firstName, lastName, IBAN, BIC, email, preferredCurrency);
        this.password = password;
    }

    /**
     * The constructor that sets the id and password of an Admin
     * @param password the password
     */
    public Admin(String password) {
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


    /**
     * checks if the password is correct
     * @param attemptedPassword the password you want to attempt
     * @return whether the password is correct
     */
    public boolean isCorrectPassword(String attemptedPassword){
        return this.password.equals(attemptedPassword);
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
