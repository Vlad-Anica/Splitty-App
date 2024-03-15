package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    @Test
    public void testNotNull() {
        Admin a = new Admin();
        assertNotNull(a);
    }
    @Test
    public void testNullGetter(){
        Admin a = new Admin();
        assertNull(a.getPassword());
    }

    @Test
    public void testGetPassword(){
        Admin a = new Admin("very secret!");
        assertEquals("very secret!", a.getPassword());
    }

    @Test
    public void testSetPassword(){
        Admin a = new Admin("very secret!");
        assertEquals("very secret!", a.getPassword());
        a.setPassword("I forgot my first one D:");
        assertEquals("I forgot my first one D:", a.getPassword());
    }

    @Test
    public void testEquals(){
        Admin a = new Admin("very secret!");
        assertEquals(a,a);
    }
    @Test
    public void testEquals2(){
        Admin a = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        Admin b = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        assertEquals(a,b);
    }

    @Test
    public void testNotEquals(){
        Admin a = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        Admin b = new Admin("first","last", "INGB 6901234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        assertNotEquals(a,b);
    }
    /*
    @Test
    public void testIdNotEquals(){
        Admin a = new Admin("very secret!");
        Admin b = new Admin("very secret!");
        assertNotEquals(a,b);
    }
    */

    @Test
    public void testPasswordNotEquals(){
        Admin a = new Admin("very secret!");
        Admin b = new Admin("verysecret!");
        assertNotEquals(a,b);
    }
    @Test
    public void testHashcode1() {
        Admin a = new Admin("");
        assertEquals(a.hashCode(), a.hashCode());
    }

    @Test
    public void testHashCode2() {
        Admin a = new Admin(":P");
        Admin b = new Admin(":PS");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testHashcode3() {
        Admin a = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        Admin b = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        assertEquals(a.hashCode(), b.hashCode());
    }
    @Test
    public void testHashcode4() {
        Admin a = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.com", Currency.EUR, "very secret!");
        Admin b = new Admin("first","last", "INGB 0001234567", "INGBNL2A", "test@email.net", Currency.EUR, "very secret!");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testCorrectPassword(){
        Admin a = new Admin("correctPassword");
        assertTrue(a.isCorrectPassword("correctPassword"));
    }
    @Test
    public void testWrongPassword(){
        Admin a = new Admin("correctPassword");
        assertFalse(a.isCorrectPassword("wrongPassword"));
    }
}
