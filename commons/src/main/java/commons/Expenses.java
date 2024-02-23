package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

/***
 * import java.util.Objects;
 * import java.util.Scanner;
 * imports that were not working
 */


@Entity
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String description;
    public double amount;
    public Date date;
    @ElementCollection
    public Person receiver;
    @ElementCollection
    public ArrayList<Person> givers;
    enum Currency{
        EURO,
        DOLLAR
    }
    public Currency currency;

    /***
     *
     * @param description description of the expense
     * @param amount how much people need to pay
     * @param date when is the expense from
     * @param receiver who is receiving the money
     * @param givers list of people who need to pay
     * @param currency the type of currency used
     */
    public Expenses(String description, double amount, Date date, Person receiver,
                    ArrayList<Person> givers, Currency currency) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.receiver = receiver;
        this.givers = givers;
        this.currency = currency;
    }

    /***
     * empty constuctor
     */
    public Expenses(){

    }

    /***
     * returns the id
     * @return
     */
    public long getId() {
        return id;
    }

    /***
     * returns the description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /***
     * returns the amount
     * @return
     */
    public double getAmount() {
        return amount;
    }

    /***
     * returns the date
     * @return
     */
    public Date getDate() {
        return date;
    }

    /***
     * returns the receiver
     * @return
     */
    public Person getReceiver() {
        return receiver;
    }

    /***
     * returns the list of people who need to pay
     * @return
     */
    public ArrayList<Person> getGivers() {
        return givers;
    }

    /***
     *  returns the currency used
     * @return
     */
    public Currency getCurrency() {
        return currency;
    }

    /***
     * updates description
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * updates amount
     * @param amount new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /***
     * updates date
     * @param date new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /***
     * updates receiver
     * @param receiver new receiver
     */
    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }

    /***
     * updates givers
     * @param givers new arraylist of givers
     */
    public void setGivers(ArrayList<Person> givers) {
        this.givers = givers;
    }

    /***
     * updates currency
     * @param currency new currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /***
     * method to be finished later that changes the amount to match the currency
     * might join this method with set currency to update amount automatically
     */
    public void convertCurrency(){

    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}

