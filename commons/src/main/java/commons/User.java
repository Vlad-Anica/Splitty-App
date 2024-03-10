package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;

    private String IBAN;
    private String BIC;
    private String email;
    private Currency preferredCurrency;

    /**
     * Constructor without arguments
     */
    public User() {

    }

    public User(String firstName, String lastName, String IBAN, String BIC, String email, Currency preferredCurrency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.email = email;
        this.preferredCurrency = preferredCurrency;
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
     * getter for firstName
     *
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter for firstName
     *
     * @param firstName the new firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter for lastName
     *
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter for lastName
     *
     * @param lastName the new lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter for IBAN
     *
     * @return the IBAN
     */
    public String getIBAN() {
        return IBAN;
    }

    /**
     * setter for IBAN
     *
     * @param IBAN the new IBAN
     */
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    /**
     * getter for BIC
     *
     * @return the BIC
     */
    public String getBIC() {
        return BIC;
    }

    /**
     * setter for BIC
     *
     * @param BIC the new BIC
     */
    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    /**
     * getter for email
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter for email
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter for preferredCurrency
     *
     * @return the preferredCurrency
     */
    public Currency getPreferredCurrency() {
        return preferredCurrency;
    }

    /**
     * setter for preferredCurrency
     *
     * @param preferredCurrency the new preferredCurrency
     */
    public void setPreferredCurrency(Currency preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }


    /**
     * checks whether it is equal to the object
     * @param o the object to check equality with
     * @return whether it is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!Objects.equals(firstName, user.firstName)) return false;
        if (!Objects.equals(lastName, user.lastName)) return false;
        if (!Objects.equals(IBAN, user.IBAN)) return false;
        if (!Objects.equals(BIC, user.BIC)) return false;
        if (!Objects.equals(email, user.email)) return false;
        return preferredCurrency == user.preferredCurrency;
    }

    /**
     * generates a hashcode
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (IBAN != null ? IBAN.hashCode() : 0);
        result = 31 * result + (BIC != null ? BIC.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (preferredCurrency != null ? preferredCurrency.hashCode() : 0);
        return result;
    }

    /**
     * generate a readable string
     * @return the readable string
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", BIC='" + BIC + '\'' +
                ", email='" + email + '\'' +
                ", preferredCurrency=" + preferredCurrency +
                '}';
    }
}
