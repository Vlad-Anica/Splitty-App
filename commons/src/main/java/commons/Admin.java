package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Random;

@Entity
public class Admin extends User {
    private static String generatedPassword;
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

    /**
     * Generates a random password when called and sets it as the static password
     * @param random a Random instance
     * @return The generated password
     */
    public static String generateRandomPassword(Random random) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*_";
        StringBuilder string = new StringBuilder();
        for(int i = 0; i<32; i++) {
            string.append(characters.charAt((int) (random.nextFloat() * characters.length())));
        }
        String result = string.toString();
        generatedPassword = result;
        return result;
    }

    /**
     * checks whether the attempt is the same as the generated password
     * @param attempt attempted password
     * @return correct or not
     */
    public static boolean isCorrectGeneratedPassword(String attempt){
        return generatedPassword.equals(attempt);
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
