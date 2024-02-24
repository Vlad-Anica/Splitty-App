package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    @Test
    public void testNotNull() {
        Admin a = new Admin();
        assertNotNull(a);
    }
}
