package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpensesTest {

    Person p = new Person("a","b");
    ArrayList<Person> pl = new ArrayList<Person>();
    Date d = new Date(0,0,0,0,0,0);
    Expenses e = new Expenses("test", 2.5, d, p, pl, Expenses.Currency.EURO );
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
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}