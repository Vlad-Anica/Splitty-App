package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void testNotNull() {
        User u = new User();
        assertNotNull(u);
    }

    @Test
    public void testEquals(){
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        User p = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        assertEquals(u,p);
    }

    @Test
    public void testNotEquals(){
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        User p = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.CHF);
        assertNotEquals(u,p);
    }

    @Test
    public void testHashcode() {
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        User p = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        assertEquals(u.hashCode(), p.hashCode());
    }

    @Test
    public void testHashCode2() {
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        User p = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.CHF);
        assertNotEquals(u.hashCode(), p.hashCode());
    }

    @Test
    public void testGetters(){
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        assertEquals("first", u.getFirstName());
        assertEquals("last", u.getLastName());
        assertEquals("INGB 0001234567", u.getIBAN());
        assertEquals("INGBNL2A", u.getBIC());
        assertEquals("test@email.com", u.getEmail());
        assertEquals(Currency.EUR, u.getPreferredCurrency());
    }
    @Test
    public void testSetters(){
        User u = new User("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR);
        assertEquals("first", u.getFirstName());
        assertEquals("last", u.getLastName());
        assertEquals("INGB 0001234567", u.getIBAN());
        assertEquals("INGBNL2A", u.getBIC());
        assertEquals("test@email.com", u.getEmail());
        assertEquals(Currency.EUR, u.getPreferredCurrency());
        u.setFirstName("firstt");
        assertEquals("firstt", u.getFirstName());
        u.setLastName("lastt");
        assertEquals("lastt", u.getLastName());
        u.setIBAN("0");
        assertEquals("0", u.getIBAN());
        u.setBIC("1");
        assertEquals("1", u.getBIC());
        u.setEmail("test@email.net");
        assertEquals("test@email.net", u.getEmail());
        u.setPreferredCurrency(Currency.USD);
        assertEquals(Currency.USD, u.getPreferredCurrency());
    }
}
