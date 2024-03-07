package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void testNotNull() {
        User u = new User();
        assertNotNull(u);
    }

    /*
    @Test
    public void testNotEquals() {
        User u1 = new User();
        User u2 = new User();
        assertNotEquals(u1, u2);
    }
    */

    @Test
    public void testHashcode() {
        User u = new User();
        assertEquals(u.hashCode(), u.hashCode());
    }

    /*
    @Test
    public void testHashCode2() {
        User u1 = new User();
        User u2 = new User();
        assertNotEquals(u1.hashCode(), u2.hashCode());
    }
    */
}
