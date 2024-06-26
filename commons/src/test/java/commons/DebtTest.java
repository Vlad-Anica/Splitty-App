package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    Debt debt;
    Person John;
    Person Bob;
    Expense expense;
    @BeforeEach
    public void initialise() {
        Event event = new Event();
        John = new Person("John", "Doe", "john@email.com",
                "NL2322220000121242", "MIDLGB22", Currency.EUR, 0.0, event, new User());
        Bob = new Person("Bob", "Bob", "bob@email.com",
                "NL2322220000121243", "MIDLGB23", Currency.EUR, 0.0, event, new User());
        expense = new Expense();
        debt = new Debt(John, Bob, new Event(), 3.0);
        John.addDebt(debt);
    }

    @Test
    void testConstructor() {
        assertEquals(John, debt.getGiver());
        assertEquals(Bob, debt.getReceiver());
        //assertEquals(expense, debt.getExpense());
        assertEquals(3.0, debt.getAmount());
        assertEquals(3.0, John.getTotalDebt());
    }

    @Test
    void setGiverTest() {
        assertEquals(John, debt.getGiver());
        Person newPerson = new Person("Jane", "Doe", "jane@email.com",
                "NL2322220000121242", "MIDLGB22", Currency.EUR, 0.0, new Event(), new User());
        debt.setGiver(newPerson);
        newPerson.addDebt(debt);
        assertEquals(newPerson, debt.getGiver());
        assertEquals(3.0, newPerson.getTotalDebt());
    }

    @Test
    void setReceiver() {
        assertEquals(Bob, debt.getReceiver());
        Person newPerson = new Person("Jane", "Doe", "jane@email.com",
                "NL2322220000121242", "MIDLGB22", Currency.EUR, 0.0, new Event(), new User());
        debt.setReceiver(newPerson);
        assertEquals(newPerson, debt.getReceiver());
    }

    @Test
    void setAmount() {
        assertEquals(3.0, debt.getAmount());
        debt.setAmount(7.0);
        assertEquals(7.0, debt.getAmount());
    }

    @Test
    void getSettled() {
        assertFalse(debt.getSettled());
    }

    @Test
    void setSettled() {
        assertFalse(debt.getSettled());
        debt.setSettled(true);
        assertTrue(debt.getSettled());
    }

    @Test
    public void hasToString() {
        var actual =  debt.toString();
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("amount"));
    }

    @Test
    public void notEqualsNull() {
        Debt a = new Debt(John, Bob, new Event(), 3.0);
        Debt b =  null;
        assertNotEquals(a, b);
    }
    @Test
    public void equalsHashCode() {
        Event e = new Event();
        var a = new Debt(John, Bob, e, 3.0);
        var b =  new Debt(John, Bob, e, 3.0);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}