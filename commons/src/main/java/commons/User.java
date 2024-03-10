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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Currency getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(Currency preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }


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
