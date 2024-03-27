package commons;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

@Entity
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
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags;
    public static final Tag foodTag= new Tag("green", "Food");
    public static final Tag entranceFeesTag = new Tag("blue", "Entrance Fees");
    public static final Tag travelTag = new Tag("red", "Travel");
    private Date date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    @JsonManagedReference(value = "event")
    private List<Person> participants;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Expense> expenses;
    private String inviteCode;


    @SuppressWarnings("Unused")
    public Event() {
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
     * Method that adds a Tag to an Event's list of Tags if not already present.
     * @param tag Tag to add to the event.
     * @return boolean, true if the Tag was added successfully, false otherwise.
     */
    public boolean addTag(Tag tag) {
        if(tag == null || this.tags.contains(tag)) {
            return false;
        }
        this.tags.add(tag);
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
        return true;
    }

    /**
     * Removes an expense from the list of expenses for an event. Returns false if
     * the expense is not within the list. Placeholder.
     * @param expense Expense representing the Expense to remove from the Event.
     * @return boolean, true if the Expense was successfully removed, false otherwise.
     */
    public boolean removeExpense(Expense expense) {
        if(expense == null || !this.getExpenses().contains(expense)) {
            return false;
        }
        this.getExpenses().remove(expense);
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
}


