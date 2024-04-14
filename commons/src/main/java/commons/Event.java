package commons;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="EVENT_ID")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="EVENT_ID")
    public long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.util.Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false, updatable = true)
    private java.util.Date updatedAt;

    /**
     * Upon initial creation of the Object, the Date will be stored.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = new java.util.Date();
        this.updatedAt = createdAt;
    }

    /**
     * Upon update, updated the Object's update date. A lot of updates here :)
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new java.util.Date();
    }
    private String name;
    private String description;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Tag> tags;
    public static final Tag foodTag= new Tag("green", "Food");
    public static final Tag entranceFeesTag = new Tag("blue", "Entrance Fees");
    public static final Tag travelTag = new Tag("red", "Travel");
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<Person> participants;
    @OneToMany (cascade = CascadeType.ALL)
    private List<Expense> expenses;
    @OneToMany (cascade = CascadeType.ALL)
    private List<Debt> debts;
    private String inviteCode;

    @SuppressWarnings("Unused")
    public Event() {
        debts = new ArrayList<>();
        participants = new ArrayList<>();
        expenses = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Constructor for event. inviteCode is left out as it is separately generated.
     * @param name String representing the Event's name
     * @param description String representing the Event's description
     * @param tags Tags representing the Events' tags
     * @param date Date representing the Event's date
     * @param participants List of Persons representing the Event's participants
     * @param expenses List of Expenses representing the Event's expenses
     */
    public Event(String name, String description, List<Tag> tags, Date date, List<Person> participants,
                 List<Expense> expenses) {
        this.name = name;
        this.description = description;
        this.tags = Objects.requireNonNullElseGet(tags, ArrayList::new);
        this.addTag(foodTag);
        this.addTag(entranceFeesTag);
        this.addTag(travelTag);
        this.date = date;
        this.participants = Objects.requireNonNullElseGet(participants, ArrayList::new);
        this.expenses = Objects.requireNonNullElseGet(expenses, ArrayList::new);
        this.inviteCode = generateInviteCode();
        debts = new ArrayList<>();
        calculateDebts(expenses);
    }

    /**
     * Method that calculates the new Debt list for the associated List of Expenses
     * @param newExpenses Expenses to analyze
     */
    public void calculateDebts(List<Expense> newExpenses) {
        List<Double> shares = new ArrayList<>();
        for (Person participant: participants) {
            shares.add(participant.getTotalDebt());
        }

        for (Expense expense: newExpenses) {
            double share = (double) expense.getAmount() / (1.0 + expense.getGivers().size());
            int receiverIndex = participants.indexOf(expense.getReceiver());
            shares.set(receiverIndex, shares.get(receiverIndex) - expense.getAmount() + share);

            for (Person giver: expense.getGivers()) {
                int giverIndex = participants.indexOf(giver);
                shares.set(giverIndex, shares.get(giverIndex) + share);
            }
        }

        removeAllDebts();
        List<Integer> giverIndexes = new ArrayList<>();
        List<Integer> receiverIndexes = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            if (shares.get(i) > 0) {
                giverIndexes.add(i);
            } else if (shares.get(i) < 0) {
                receiverIndexes.add(i);
            }
        }

        for (int i = 0; i < giverIndexes.size(); i++) {
            double giverShare = shares.get(giverIndexes.get(i));
            Person giver = participants.get(giverIndexes.get(i));

            while (giverShare < 0) {
                double receiverShare = shares.get(receiverIndexes.get(i));
                Person receiver = participants.get(receiverIndexes.get(i));

                if (receiverShare > -giverShare) {
                    addDebt(new Debt(giver, receiver, this, -giverShare));
                    shares.set(receiverIndexes.get(i), receiverShare + giverShare);
                } else {
                    addDebt(new Debt(giver, receiver, this, receiverShare));
                    giverShare += receiverShare;
                }
            }
        }

    }

    public List<Debt> getDebts() {
        return debts;
    }


    /**
     * Method that removes a Debt from its association to an Event,
     * @param debt Debt to remove.
     */
    public void removeDebt(Debt debt) {
        if (!debts.contains(debt)) {
            return;
        }
        debt.getReceiver().removeDebt(debt);
        debt.getGiver().removeDebt(debt);
        debts.remove(debt);
    }
    /**
     *
     * Method that adds a Debt to an Event,
     * @param debt Debt to add.
     */
    public void addDebt(Debt debt) {
        debt.getReceiver().addDebt(debt);
        debt.getGiver().addDebt(debt);
        debts.add(debt);
    }

    /**
     * Method that removes all associated debts from an Event.
     */
    public void removeAllDebts() {
        for (Debt debt: debts) {
            debt.getReceiver().removeDebt(debt);
            debt.getGiver().removeDebt(debt);
        }
        debts = new ArrayList<>();
    }

    /**
     * Getter for an Event's id.
     * @return long representing the Event's id
     */
    public long getId() {
        return id;
    }

    /**
     * Method that returns creation time of the object.
     * @return Util Date
     */
    public java.util.Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Method that returns update time of the object.
     * @return Util Date
     */
    public java.util.Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Getter for an Event's name.
     * @return String representing the Event's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for an Event's name.
     * @param name String representing the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for an Event's description.
     * @return String representing the Event's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for an Event's description.
     * @param description String representing the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for an Event's tag.
     * @return List of Tags representing the Events' tags
     */
    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * Setter for an Event's tag.
     * @param tags List of Tags representing the new tags for the event
     */
    public void setTags(List<Tag> tags) {
        this.tags = Objects.requireNonNullElseGet(tags, ArrayList::new);
        this.addTag(foodTag);
        this.addTag(entranceFeesTag);
        this.addTag(travelTag);
    }

    /**
     * Method that determines whether a Tag is present in the Event's list of Tags.
     * @param tag Tag to check
     * @return boolean, true if the Tag is in the Event's List. False otherwise.
     */
    public boolean containsTag(Tag tag) {
        for(Tag eventTag : this.getTags()) {
            if(tag.equals(eventTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that adds a Tag to an Event's list of Tags if not already present.
     * @param tag Tag to add to the event.
     * @return boolean, true if the Tag was added successfully, false otherwise.
     */
    public boolean addTag(Tag tag) {
        if(tag == null || this.tags.contains(tag)) {
            return false;
        }
        for (Tag tag1: tags) {
            if (tag.getType().equals(tag1.getType())) {
                return false;
            }
        }
        this.tags.add(tag);
        return true;
    }

    /**
     * Method that removes a Tag from an Event if it is deprecated. Does not work if the Tag is currently in use.
     * @param tag Tag to remove
     * @return boolean, true if the Tag was removed, false otherwise
     */
    public boolean deprecateTag(Tag tag) {
        if(tag == null || !this.getTags().contains(tag)) {
            return false;
        }
        for(Expense expense : this.getExpenses()) {
            if(expense.getTag().equals(tag)) {
                return false;
            }
        }
        this.getTags().remove(tag);
        return true;
    }

    /**
     * Getter for an Event's date.
     * @return Date representing the Event's date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for an Event's date.
     * @param date Date representing the Event's new date.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for an Event's participants.
     * @return List of Persons representing the Event's participants
     */
    public List<Person> getParticipants() {
        return participants;
    }

    /**
     * Setter for an Event's participants
     * @param participants List of Persons representing the Event's new List of participants
     */
    public void setParticipants(List<Person> participants) {
        this.participants = Objects.requireNonNullElseGet(participants, ArrayList::new);
    }

    /**
     * Getter for an Event's expenses. Placeholder.
     * @return List of Expenses representing the Event's logged expenses.
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Setter for an Event's expenses. Placeholder.
     * @param expenses List of Expenses representing the new List of Expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Getter for an Event's unique inviteCode.
     * @return String representing the Event's invite code
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Refresh for an Event's unique inviteCode. Creates a new randomly assigned inviteCode.
     */
    public void refreshInviteCode() {
        this.inviteCode = generateInviteCode();
    }

    /**
     * Determines whether a person is currently attending an event.
     * @param person Person representing the participant to check.
     * @return boolean, true if the Person is currently attending the event, false otherwise.
     */
    public boolean isAttending(Person person) {
        if(person == null) {
            return false;
        }
        return this.participants.contains(person);
    }

    /**
     * Adds a person to the list of participants attending an event. Returns true if successful.
     * @param person Person representing the participant to add to the Event.
     * @return boolean, true if the Person was added successfully, false otherwise.
     */
    public boolean addParticipant(Person person) {
        if(person == null || this.isAttending(person)) {
            return false;
        }
        this.getParticipants().add(person);
        person.setEvent(this);
        return true;
    }

    /**
     * Removes a person from the list of participants attending an event. Returns false if
     * the person does not attend it.
     * @param person Person representing the participant to remove from the Event.
     * @return boolean, true if a Person was successfully removed, false otherwise.
     */
    public boolean removeParticipant(Person person) {
        if(person == null || !this.getParticipants().contains(person)) {
            return false;
        }
        this.getParticipants().remove(person);
        return true;
    }

    /**
     * Method that severs the link between an Event and Person.
     * @param person Person to remove from the Event
     */
    public void severPersonConnection(Person person) {
        List<Expense> expenseList = new ArrayList<>(getExpenses());
        for(Expense expense : expenseList) {
            if(expense.getInvolved().contains(person)) {
                if(expense.getReceiver().equals(person)) {
                    this.removeExpense(expense);
                } else {
                    List<Person> persons = expense.getInvolved();
                    persons.remove(expense.getReceiver());
                    if(persons.size() == 1) {
                        this.removeExpense(expense);
                    }
                }
            }
        }
        this.removeParticipant(person);
    }

    /**
     * Determines whether an expense is part of an event.
     * @param expense Expense to check.
     * @return boolean, true if the Expense is in the event, false otherwise.
     */
    public boolean containsExpense(Expense expense) {
        if(expense == null) {
            return false;
        }
        return this.expenses.contains(expense);
    }

    /**
     * Adds an expense to the list of expenses for an event. Returns true if successful.
     * Cannot add duplicate Expenses. Placeholder.
     * @param expense Expense representing the Expense to add to the Event.
     * @return boolean, true if the Expense was sucessfully added, false otherwise.
     */
    public boolean addExpense(Expense expense) {
        if(expense == null || this.getExpenses().contains(expense)) {
            return false;
        }
        this.getExpenses().add(expense);
        calculateDebts(new ArrayList<>(List.of(expense)));
        return true;
    }

    /**
     * Removes an expense from the list of expenses for an event. Returns false if
     * the expense is not within the list. Severs Debts associated with it.
     * @param expense Expense representing the Expense to remove from the Event.
     * @return boolean, true if the Expense was successfully removed, false otherwise.
     */
    public boolean removeExpense(Expense expense) {
        if(expense == null || !this.getExpenses().contains(expense)) {
            return false;
        }
        removeAllDebts();
        this.getExpenses().remove(expense);
        calculateDebts(new ArrayList<>(List.of(expense)));
        return true;
    }

    /**
     * Generated an invitation code for the event.
     * @return String representing the invite code.
     */
    private static String generateInviteCode() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder string = new StringBuilder();
        Random random = new Random();
        while(string.length() < 8) {
            int i = (int) (random.nextFloat() * characters.length());
            string.append(characters.charAt(i));
        }
        return string.toString();
    }

    /**
     * Equals method for the Event class. Placeholder.
     * @param o Object to compare with.
     * @return boolean, true if o is equal with this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(tags, event.tags) && Objects.equals(date, event.date) && Objects.equals(participants, event.participants) && Objects.equals(expenses, event.expenses) && Objects.equals(inviteCode, event.inviteCode);
    }

    /**
     * Hashcode method for the Event class.
     * @return int representing the hashCode of the respective Event object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tags, date, participants, expenses);
    }

    @Override
    public String toString() {
        if (getDate() == null) return getName();

        LocalDate shownDate = getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String parsedDate = shownDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
        return getName() + ", " + parsedDate;

    }
}


