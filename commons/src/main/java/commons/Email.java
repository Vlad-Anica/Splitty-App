package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

@Entity
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String address;

    /**
     * constructor for email
     * @param address the address (name) of the email
     */
    public Email(String address) {
        this.address = address;
    }

    /**
     * constructor for email with no args
     */
    public Email() {}

    /**
     * getter for id
     * @return id of the email
     */
    public long getId() {
        return id;
    }


    /**
     * getter for address
     * @return address (name) of the email
     */
    public String getAddress() {
        return address;
    }

    /**
     * setter for address - changes the address(name) of the email
     * @param address new name
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * checks if it is equal to an Object o
     * @param obj the object
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * hash function
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * checks if its fields are equal to that of another object, without looking at the ids
     * @param o object to check
     * @return true if equal, false otherwise
     */
    public boolean equalsWithoutId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    /**
     * turns the object into a readable string
     * @return the string which represents the object
     */
    @Override
    public String toString() {
        return "Email: " + address + "; " + id;
    }
}
