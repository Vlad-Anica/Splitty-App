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
    public void testGetId(){
        Admin a = new Admin(13, "very secret!");
        assertEquals(13, a.getId());

    }

    @Test
    public void testGetPassword(){
        Admin a = new Admin(13, "very secret!");
        assertEquals("very secret!", a.getPassword());
    }

    @Test
    public void testSetId(){
        Admin a = new Admin(13, "very secret!");
        assertEquals(13, a.getId());
        a.setId(69);
        assertEquals(69,a.getId());
    }

    @Test
    public void testSetPassword(){
        Admin a = new Admin(13, "very secret!");
        assertEquals("very secret!", a.getPassword());
        a.setPassword("I forgot my first one D:");
        assertEquals("I forgot my first one D:", a.getPassword());
    }

    @Test
    public void testEquals(){
        Admin a = new Admin(13, "very secret!");
        Admin b = new Admin(13, "very secret!");
        assertEquals(a,b);
    }

    @Test
    public void testIdNotEquals(){
        Admin a = new Admin(13, "very secret!");
        Admin b = new Admin(12, "very secret!");
        assertNotEquals(a,b);
    }

    @Test
    public void testPasswordNotEquals(){
        Admin a = new Admin(13, "very secret!");
        Admin b = new Admin(13, "verysecret!");
        assertNotEquals(a,b);
    }
    @Test
    public void testHashcode1() {
        Admin a = new Admin(1 ,"");
        Admin b = new Admin(1, "");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testHashCode2() {
        Admin a = new Admin(1, ":P");
        Admin b = new Admin(13, ":P");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testCorrectPassword(){
        Admin a = new Admin(13, "correctPassword");
        assertTrue(a.isCorrectPassword("correctPassword"));
    }
    @Test
    public void testWrongPassword(){
        Admin a = new Admin(13, "correctPassword");
        assertFalse(a.isCorrectPassword("wrongPassword"));
    }
}
