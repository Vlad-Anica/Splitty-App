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
    @BeforeEach
    public void createStartup() {
        p1 = new Person("Adam", "James",
                "email@email.com", "NL33", "MIDLGB22");
        p2 = new Person("John", "Doe", "email2@email.com", "RO420", "MIDLGB22");
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        ArrayList<Expense> expenses = new ArrayList<>();
        persons1 = new ArrayList<>();
        persons2 = new ArrayList<>();
        persons1.add(p1);
        persons2.add(p2);
        date = new Date(0,0,0,0,0,0);
        Expense e1 = new Expense("test1", 2.5, date, p1, persons2, Expense.Currency.EURO );
        Expense e2 = new Expense("test2", 2.5, date, p2, persons1, Expense.Currency.EURO );
        expenses.add(e1);
        expenses.add(e2);
        Tag tag = new Tag("red", "food");
        ev = new Event("Dinner Party", "Bob's Celebration Dinner", tag, new Date(1, 14, 5, 2006, 4, 30 ), persons, expenses);
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
        assertEquals(new Tag("red", "food"), ev.getTag());
    }

    @Test
    public void testTagSetter() {
        ev.setTag(new Tag("cowabunga", "food"));
        assertEquals(new Tag("cowabunga", "food"), ev.getTag());
    }

    @Test
    public void testDateGetter() {
        assertEquals(new Date(1, 14, 5, 2006, 4, 30), ev.getDate());
    }

    @Test
    public void testDateSetter() {
        ev.setDate(new Date(2, 3, 4, 5, 6, 7));
        assertEquals(new Date(2, 3, 4, 5, 6, 7), ev.getDate());
    }

    @Test
    public void testPersonsGetter() {
        Person pe1 = new Person("Adam", "James",
                "email@email.com", "NL33", "MIDLGB22");
        Person pe2 = new Person("John", "Doe",
                "email2@email.com", "RO420", "MIDLGB22");
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(pe1);
        personList.add(pe2);
        assertEquals(personList, ev.getParticipants());
    }

    @Test
    public void testPersonsSetter() {
        Person pe1 = new Person("Adam", "Connor",
                "exxmail@email.com", "NL33", "MIDLGB22");
        Person pe2 = new Person("John", "Does",
                "emaidkddl2@email.com", "RO4424220", "MIDLGB22");
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(pe1);
        personList.add(pe2);
        ev.setParticipants(personList);
        assertEquals(personList, ev.getParticipants());
    }

    @Test
    public void testExpensesGetter() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        Expense ex1 = new Expense("test1", 2.5, date, p1, persons2, Expense.Currency.EURO );
        Expense ex2 = new Expense("test2", 2.5, date, p2, persons1, Expense.Currency.EURO );
        expenseList.add(ex1);
        expenseList.add(ex2);
        assertEquals(expenseList, ev.getExpenses());
    }

    @Test
    public void testExpensesSetter() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        Expense ex1 = new Expense("test1", 2.5, date, p1, persons2, Expense.Currency.EURO );
        Expense ex2 = new Expense("test2", 2.5, date, p2, persons1, Expense.Currency.EURO );
        expenseList.add(ex1);
        expenseList.add(ex2);
        ev.setExpenses(expenseList);
        assertEquals(expenseList, ev.getExpenses());
    }

    @Test
    public void inviteCodeTest() {
        String previousCode = ev.getInviteCode();
        ev.refreshInviteCode();
        assertNotEquals(previousCode, ev.getInviteCode());
    }

    /*
    @Test
    public void equalsTest() {
        Person pe1 = new Person("Adam", "James",
                "email@email.com", "NL33");
        Person pe2 = new Person("John", "Doe",
                "email2@email.com", "RO420", "MIDLGB22");
        String e1 = "food";
        String e2 = "waiters";
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(p1);
        personList.add(p2);
        ArrayList<String> expenseList = new ArrayList<>();
        expenseList.add(e1);
        expenseList.add(e2);
        Event ev2 = new Event("Dinner Party", "Bob's Celebration Dinner", "celebration", new Date(1, 14, 5, 2006, 4, 30 ), personList, expenseList);
        assertEquals(ev2, ev);
    }
    */


}
