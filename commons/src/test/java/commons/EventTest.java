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
        date = new Date(0,0,0,0,0,0);
        Tag t = new Tag("blue", "food");
        Expense e1 = new Expense("test1", 2.5, date, p1, List.of(debt1), Currency.EUR, t  );
        Expense e2 = new Expense("test2", 2.5, date, p2, List.of(debt2), Currency.EUR, t );
        expenses.add(e1);
        expenses.add(e2);
        debt1.setExpense(e1);
        debt2.setExpense(e2);
        Tag tag = new Tag("red", "food");
        ev = new Event("Dinner Party", "Bob's Celebration Dinner", tag,
                new Date(1, 14, 5, 2006, 4, 30 ),
                persons, expenses);
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
        Expense ex1 = new Expense("test1", 2.5, date, p1, List.of(debt1), Currency.EUR, t  );
        Expense ex2 = new Expense("test2", 2.5, date, p2, List.of(debt2), Currency.EUR, t );
        expenseList.add(ex1);
        expenseList.add(ex2);
        assertEquals(expenseList, ev.getExpenses());
    }

    @Test
    public void testExpensesSetter() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        Tag t = new Tag("blue", "food");
        Expense ex1 = new Expense("test1", 2.5, date, p1, List.of(debt1), Currency.EUR, t  );
        Expense ex2 = new Expense("test2", 2.5, date, p2, List.of(debt2), Currency.EUR, t );
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
        assertFalse(ev.isAttending(p3));
        assertTrue(ev.addParticipant(p3));
        assertTrue(ev.isAttending(p3));
    }

    @Test
    public void removeParticipantTest() {
        Person p3 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",
                Currency.EUR, 0.0, new Event(), new User());
        ev.addParticipant(p3);
        assertTrue(ev.removeParticipant(p3));
        assertFalse(ev.isAttending(p3));
    }

    @Test
    public void addExpenseTest() {
        Date dateTest = new Date(1,2,3,4,5,6);
        Person p3 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",
                Currency.EUR, 0.0, new Event(), new User());
        debts.add(debt2);
        Tag t = new Tag("blue", "food");
        Expense e3 = new Expense("amongUs", 5, dateTest, p3, debts, Currency.USD, t  );
        assertFalse(ev.containsExpense(e3));
        assertTrue(ev.addExpense(e3));
        assertTrue(ev.containsExpense(e3));
    }

    @Test
    public void removeExpenseTest() {
        Date dateTest = new Date(1,2,3,4,5,6);
        Person p3 = new Person("Senator", "Armstrong",
                "usa@email.com", "AM420", "NITUL42",
                Currency.EUR, 0.0, new Event(), new User());
        debts.add(debt2);
        Tag t = new Tag("blue", "food");
        Expense e3 = new Expense("amongUs", 5, dateTest, p3, debts, Currency.USD, t  );
        ev.addExpense(e3);
        assertTrue(ev.removeExpense(e3));
        assertFalse(ev.containsExpense(e3));
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
