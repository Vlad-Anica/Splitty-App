package commons;


import jakarta.persistence.*;

import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private String name;
    private String description;

    //change once Tag class gets created
    private String tag;

    @OneToOne
    private Date date;
    @OneToMany
    private List<Person> participants;
    //change once Expense class gets created
    @ElementCollection
    private List<String> expenses;

    private String inviteCode;

    @SuppressWarnings("Unused")
    protected Event() {
    }

    /**
     * Constructor for event. inviteCode is left out as it is separately generated.
     * @param id
     * @param name
     * @param description
     * @param tag
     * @param date
     * @param participants
     * @param expenses
     */
    public Event(long id, String name, String description, String tag, Date date, List<Person> participants,
                 List<String> expenses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tag = tag;
        this.date = date;
        if(participants == null) {
            this.participants = new ArrayList<Person>();
        } else {
            this.participants = participants;
        }
        this.expenses = expenses;
        this.inviteCode = generateInviteCode();
    }

    /**
     * Getter for an Event's id
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for an Event's id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for an Event's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for an Event's name
     * @return
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for an Event's description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for an Event's description
     * @return
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for an Event's tag. Placeholder.
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setter for an Event's tag. Placeholder.
     * @return
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Getter for an Event's date
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for an Event's date
     * @return
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for an Event's participants
     * @return
     */
    public List<Person> getParticipants() {
        return participants;
    }

    /**
     * Setter for an Event's participants
     * @return
     */
    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }

    /**
     * Getter for an Event's expenses. Placeholder.
     * @return
     */
    public List<String> getExpenses() {
        return expenses;
    }

    /**
     * Setter for an Event's expenses. Placeholder.
     * @return
     */
    public void setExpenses(List<String> expenses) {
        this.expenses = expenses;
    }

    /**
     * Getter for an Event's unique inviteCode
     * @return
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Refresh for an Event's unique inviteCode
     */
    public void refreshInviteCode() {
        this.inviteCode = generateInviteCode();
    }

    /**
     * Adds a person to the list of participants attending an event. Returns true if successful.
     * @param person
     * @return
     */
    public boolean addParticipant(Person person) {
        if(person == null || this.getParticipants().contains(person)) {
            return false;
        }
        this.getParticipants().add(person);
        return true;
    }

    /**
     * Removes a person from the list of participants attending an event. Returns false if
     * the person does not attend it.
     * @param person
     * @return
     */
    public boolean removeParticipant(Person person) {
        if(person == null || !this.getParticipants().contains(person)) {
            return false;
        }
        this.getParticipants().remove(person);
        return true;
    }

    /**
     * Adds an expense to the list of expenses for an event. Returns true if successful. Placeholder.
     * @param expense
     * @return
     */
    public boolean addExpense(String expense) {
        if(expense == null || this.getExpenses().contains(expense)) {
            return false;
        }
        this.getExpenses().add(expense);
        return true;
    }

    /**
     * Removes an expense from the list of expenses for an event. Returns false if
     * the expense is not within the list. Placeholder.
     * @param expense
     * @return
     */
    public boolean removeExpense(String expense) {
        if(expense == null || !this.getExpenses().contains(expense)) {
            return false;
        }
        this.getExpenses().remove(expense);
        return true;
    }

    /**
     * Generated an invite code for the event.
     * @return String representing the invite code.
     */
    private static String generateInviteCode() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder string = new StringBuilder();
        Random rndm = new Random();
        while(string.length() < 8) {
            int i = (int) (rndm.nextFloat() * characters.length());
            string.append(characters.charAt(i));
        }
        String code = string.toString();
        return code;
    }

    /**
     * Equals method for the Event class. Placeholder.
     * @param o Object to compare with.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(tag, event.tag) && Objects.equals(date, event.date) && Objects.equals(participants, event.participants) && Objects.equals(expenses, event.expenses) && Objects.equals(inviteCode, event.inviteCode);
    }

    /**
     * Hashcode method for the Event class.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tag, date, participants, expenses, inviteCode);
    }
}
