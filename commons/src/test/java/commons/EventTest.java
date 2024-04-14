package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    Event ev;
    Person p1;
    Person p2;
    Date date;
    ArrayList<Person> persons1;
    ArrayList<Person> persons2;
    List<Debt> debts;
    Debt debt1, debt2;
    Tag t;
    ArrayList<Tag> tags;
    Expense e1;
    Expense e2;
    @BeforeEach
    public void createStartup() {
        p1 = new Person("Adam", "James",
                "email@email.com", "NL33", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        p2 = new Person("John", "Doe",
                "email2@email.com", "RO420", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        ArrayList<Person> persons = new ArrayList<>();
        debt1 = new Debt(p2, p1, null, 2.5);
        debt2 = new Debt(p1, p2, null, 2.5);
        persons.add(p1);
        persons.add(p2);
        ArrayList<Expense> expenses = new ArrayList<>();
        persons1 = new ArrayList<>();
        persons2 = new ArrayList<>();
        debts = new ArrayList<>();
        persons1.add(p1);
        persons2.add(p2);
        date = new Date(424242);
        t = new Tag("blue", "food");
        tags = new ArrayList<>();
        tags.add(t);
        e1 = new Expense("test1", 2.5, date, p1, persons1, Currency.EUR, t );
        e2 = new Expense("test2", 2.5, date, p2, persons2, Currency.EUR, t );
        expenses.add(e1);
        expenses.add(e2);

        ev = new Event("Dinner Party", "Bob's Celebration Dinner", tags,
                new Date(424242),
                persons, expenses);
        debt1.setEvent(ev);
        debt2.setEvent(ev);
    }

    @Test
    public void testNameGetter() {
        assertEquals("Dinner Party", ev.getName());
    }

    @Test
    public void testNameSetter() {
        ev.setName("Pool Party");
        assertEquals("Pool Party", ev.getName());
    }

    @Test
    public void testDescriptionGetter() {
        assertEquals("Bob's Celebration Dinner", ev.getDescription());
    }

    @Test
    public void testDescriptionSetter() {
        ev.setDescription("Dan's Celebration Dinner");
        assertEquals("Dan's Celebration Dinner", ev.getDescription());
    }

    @Test
    public void testTagGetter() {
        ArrayList<Tag> tags2 = new ArrayList<>();
        tags2.add(t);
        tags2.add(Event.foodTag);
        tags2.add(Event.entranceFeesTag);
        tags2.add(Event.travelTag);
        assertEquals(tags2, ev.getTags());
    }

    @Test
    public void testTagsSetter() {
        ArrayList<Tag> tags2 = new ArrayList<>();
        Tag t = new Tag("cowabunga", "food");
        tags2.add(t);
        tags2.add(Event.foodTag);
        tags2.add(Event.entranceFeesTag);
        tags2.add(Event.travelTag);
        ev.setTags(tags2);
        assertEquals(tags2, ev.getTags());
    }

    @Test
    public void testDateGetter() {
        assertEquals(date, ev.getDate());
    }

    @Test
    public void testDateSetter() {
        ev.setDate(new Date(433));
        assertEquals(new Date(433), ev.getDate());
    }

    @Test
    public void testPersonsGetter() {
        Person pe1 = new Person("Adam", "James",
                "email@email.com", "NL33", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        Person pe2 = new Person("John", "Doe",
                "email2@email.com", "RO420", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(pe1);
        personList.add(pe2);
        assertEquals(personList, ev.getParticipants());
    }

    @Test
    public void testPersonsSetter() {
        Person pe1 = new Person("Adam", "Connor",
                "exxmail@email.com", "NL33", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        Person pe2 = new Person("John", "Does",
                "emaidkddl2@email.com", "RO4424220", "MIDLGB22",
                Currency.EUR, 0.0, new Event(), new User());
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(pe1);
        personList.add(pe2);
        ev.setParticipants(personList);
        assertEquals(personList, ev.getParticipants());
    }

    @Test
    public void testExpensesGetter() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        Tag t = new Tag("blue", "food");
        Expense ex1 = new Expense("test1", 2.5, date, p1, persons1, Currency.EUR, t );
        Expense ex2 = new Expense("test2", 2.5, date, p2, persons2, Currency.EUR, t );
        expenseList.add(ex1);
        expenseList.add(ex2);
        assertEquals(expenseList, ev.getExpenses());
    }

    @Test
    public void testExpensesSetter() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        Tag t = new Tag("blue", "food");
        Expense ex1 = new Expense("test1", 2.5, date, p1, persons1, Currency.EUR, t  );
        Expense ex2 = new Expense("test2", 2.5, date, p2, persons2, Currency.EUR, t );
        expenseList.add(ex1);
        expenseList.add(ex2);
        ev.setExpenses(expenseList);
        assertEquals(expenseList, ev.getExpenses());
    }

    @Test
    public void addParticipantTest() {
        Person p3 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",
                Currency.EUR, 0.0, new Event(), new User());
        p3.setId(3);
        assertFalse(ev.isAttending(p3));
        assertTrue(ev.addParticipant(p3));
        assertTrue(ev.isAttending(p3));
    }
    @Test
    public void addParticipantNullTest() {
        assertFalse(ev.addParticipant(null));
    }

    @Test
    public void isAttendingNullTest() {
        assertFalse(ev.isAttending(null));
    }

    @Test
    public void removeParticipantTest() {
        Person p3 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",
                Currency.EUR, 0.0, new Event(), new User());
        p3.setId(3);
        ev.addParticipant(p3);
        assertTrue(ev.removeParticipant(p3));
        assertFalse(ev.isAttending(p3));
    }

    @Test
    public void removeNullParticipantTest() {
        assertFalse(ev.removeParticipant(null));
    }

    @Test
    public void removeNonParticipantTest() {
        Person p1 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",Currency.EUR, 0.0, new Event(), new User());
        p1.setId(3);
        assertFalse(ev.removeParticipant(p1));
    }

    @Test
    public void addExpenseNullTest() {
        assertFalse(ev.addExpense(null));
    }

    @Test
    public void containsExpenseNullTest() {
        assertFalse(ev.containsExpense(null));
    }

    @Test
    public void removeExpenseNullTest() {
        assertFalse(ev.removeExpense(null));
    }

    @Test
    public void inviteCodeTest() {
        String previousCode = ev.getInviteCode();
        ev.refreshInviteCode();
        assertNotEquals(previousCode, ev.getInviteCode());
    }

    @Test
    public void eventHashCodeEqualsSameTest() {
        assertEquals(ev.hashCode(), ev.hashCode());
    }


}
