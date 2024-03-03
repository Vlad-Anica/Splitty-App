package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Person giver;
    @ManyToOne
    private Person receiver;
    @ManyToOne
    private Expense expense;
    private double amount;
    private boolean settled;
    public Debt(Person giver, Person receiver, Expense expense, double amount) {
        this.giver = giver;
        this.receiver = receiver;
        this.expense = expense;
        this.amount = amount;
        this.settled = false;
    }
    public Debt() {

    }
    public long getId() {
        return id;
    }
    public Person getGiver() {
        return giver;
    }
    public Person getReceiver() {
        return receiver;
    }
    public Expense getExpense() {
        return expense;
    }
    public double getAmount() {
        return amount;
    }
    public boolean getSettled(){
        return settled;
    }
    public void setGiver(Person giver) {
        this.giver = giver;
    }
    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }
    public void setExpense(Expense expense) {
        this.expense = expense;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setSettled(Boolean value){
        settled = value;
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
