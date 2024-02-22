package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    @Test
    public void testNotNull() {
        User u = new User();
        assertNotNull(u);
    }

    @Test
    void testEquals() {
        User u1 = new User();
        User u2 = new User();
        assertEquals(u1, u2);
    }

    @Test
    void testHashcode() {
        User u1 = new User();
        User u2 = new User();
        assertEquals(u1.hashCode(), u2.hashCode());
    }
}
