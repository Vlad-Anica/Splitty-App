package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    Person p = new Person("a","b");
    ArrayList<Person> pl = new ArrayList<Person>();
    Date d = new Date(0,0,0,0,0,0);
    Expense e = new Expense("test", 2.5, d, p, pl, Expense.Currency.EURO );
    Expense e2 = new Expense("test3", 2.5, d, p, pl, Expense.Currency.EURO );
    Expense e3 = new Expense("test", 2.5, d, p, pl, Expense.Currency.EURO );
    @Test
    void getId() {
    }

    @Test
    void getDescription() {
        assertEquals("test", e.getDescription());
    }

    @Test
    void getAmount() {
        assertEquals(2.5, e.getAmount());
    }

    @Test
    void getDate() {
        assertTrue(d.equals(e.getDate()));
    }

    @Test
    void getReceiver() {
    }

    @Test
    void getGivers() {
    }

    @Test
    void getCurrency() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void setAmount() {
    }

    @Test
    void setDate() {
    }

    @Test
    void setReceiver() {
    }

    @Test
    void setGivers() {
    }

    @Test
    void setCurrency() {
    }

    @Test
    void testEquals() {
        assertFalse(e.equals(e2));
    }

    @Test
    void testHashCode() {
        assertTrue(e.hashCode() == e3.hashCode());
    }

    @Test
    void testToString() {
    }
}