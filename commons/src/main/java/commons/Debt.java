package commons;

import jakarta.persistence.*;

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
    private Expenses expense;
    private double amount;
    public Debt(Person giver, Person receiver, Expenses expense, double amount) {
        this.giver = giver;
        this.receiver = receiver;
        this.expense = expense;
        this.amount = amount;
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
    public Expenses getExpense() {
        return expense;
    }
    public double getAmount() {
        return amount;
    }
    public void setGiver(Person giver) {
        this.giver = giver;
    }
    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }
    public void setExpense(Expenses expense) {
        this.expense = expense;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
