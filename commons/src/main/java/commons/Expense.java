package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/***
 * import java.util.Objects;
 * import java.util.Scanner;
 * imports that were not working
 */


@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String description;
    public double amount;
    @ManyToOne(cascade = CascadeType.PERSIST)
    public Date date;
    @ManyToOne(cascade = CascadeType.PERSIST)
    public Person receiver;
    @OneToMany
    public List<Debt> debtList;
    public Currency currency;
    public Tag tag;

    /***
     *
     * @param description description of the expense
     * @param amount how much people need to pay
     * @param date when is the expense from
     * @param receiver who is receiving the money
     * @param debtList list of debts split between the participants
     * @param currency the type of currency used
     * @param tag the tag of the expense
     */
    public Expense(String description, double amount, Date date, Person receiver,
                   List<Debt> debtList, Currency currency, Tag tag) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.receiver = receiver;
        this.debtList = debtList;
        this.currency = currency;
        this.tag = tag;
    }

    /***
     * empty constuctor
     */
    public Expense(){

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
    public List<Debt> getDebtList() {
        return debtList;
    }

    /***
     *  returns the currency used
     * @return
     */
    public Currency getCurrency() {
        return currency;
    }

    /***
     * returns the expense's tag
     * @return
     */
    public Tag getTag() {
        return tag;
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
     * @param debtList new arraylist of debts
     */
    public void setDebtList(List<Debt> debtList) {
        this.debtList = debtList;
    };

    /***
     * updates currency
     * @param currency new currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
        convertCurrency(currency);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /***
     * updates the amount number depending on the currency
     */
    public void convertCurrency(Currency newCurrency){
        switch (this.currency){
            case EUR: {
                switch(newCurrency){
                    case EUR: {
                        break;
                    }
                    case USD: {
                        this.amount = this.amount * 1.09;
                    }
                    case CHF:{
                        this.amount = this.amount * 0.96;
                    }
                }
            }
            case USD: {
                switch(newCurrency){
                    case EUR: {
                        this.amount = this.amount * 0.92;
                    }
                    case USD: {
                        break;
                    }
                    case CHF:{
                        this.amount = this.amount * 0.88;
                    }
                }
            }
            case CHF:{
                switch(newCurrency){
                    case EUR: {
                        this.amount = this.amount * 1.04;
                    }
                    case USD: {
                        this.amount = this.amount * 1.13;
                    }
                    case CHF:{
                        break;
                    }
                }
            }
        }

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

