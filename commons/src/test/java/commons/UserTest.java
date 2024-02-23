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
    public void testGetId() {
        User u = new User(13);
        assertEquals(13, u.getId());
    }

    @Test
    public void testSetId() {
        User u = new User(1);
        u.setId(13);
        assertEquals(13, u.getId());
    }

    @Test
    public void testEquals1() {
        User u1 = new User(0);
        User u2 = new User(0);
        assertEquals(u1, u2);
    }

    @Test
    public void testEquals2() {
        User u1 = new User(0);
        User u2 = new User(1);
        assertNotEquals(u1, u2);
    }

    @Test
    public void testHashcode1() {
        User u1 = new User(1);
        User u2 = new User(1);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    public void testHashCode2() {
        User u1 = new User(1);
        User u2 = new User(13);
        assertNotEquals(u1.hashCode(), u2.hashCode());
    }
}
