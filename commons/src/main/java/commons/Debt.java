package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "DEBT")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    @ManyToOne
    @JoinColumn(name = "GIVER_ID")
    @JsonBackReference
    private Person giver;
    @ManyToOne
    @JoinColumn(name = "RECEIVER_ID")
    @JsonBackReference
    private Person receiver;
    @ManyToOne
    @JoinColumn(name = "EXPENSE_ID")
    private Expense expense;
    @Column(name = "amount")
    private double amount;
    @Column(name = "settled")
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
