package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    private static final Email email1 = new Email("1");
    private static final Email email2 = new Email("2");
    private static final Email email3 = new Email("1");
    private static final Email dummyEmail = new Email("1");
    private static final String beginEmail1 = "Email: 1; ";

    @Test
    void testConstructor() {
        Email email = new Email("emailname");
        assertTrue(email.getAddress().equals("emailname"));
    }

    @Test
    void getAddress() {
        assertTrue(email1.getAddress().equals("1"));
    }

    @Test
    void setAddress() {
        dummyEmail.setAddress("4");
        assertTrue(dummyEmail.getAddress().equals("4"));
    }

    @Test
    void testEquals() {
        assertFalse(email1.equals(email2));
    }

    @Test
    void testEqualsTrue() {
        assertTrue(email1.equals(email1));
    }

    @Test
    void testHashCode() {
        assertTrue(email1.hashCode() == email3.hashCode());
    }

    @Test
    void equalsWithoutIdFalse() {
        assertFalse(email1.equalsWithoutId(email2));
    }

    @Test
    void equalsWithoutIdTrue() {
        assertTrue(email1.equalsWithoutId(email3));
    }

    @Test
    void testToString() {
        assertTrue(email1.toString().startsWith(beginEmail1));
    }
}