package commons;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    @Test
    public void testNotNull() {
        Admin a = new Admin();
        assertNotNull(a);
    }
    @Test
    public void TestGenerateRandomPassword(){
        assertEquals("pxHhjLd8gtNHRh**!1%CJM9QAo10c0&d", Admin.generateRandomPassword(new Random(0)));
    }

    @Test
    public void TestCorrectGenerateRandomPassword(){
        String password = Admin.generateRandomPassword(new Random());
        assertTrue(Admin.isCorrectGeneratedPassword(password));
    }
    @Test
    public void TestWrongGenerateRandomPassword(){
        String password = Admin.generateRandomPassword(new Random());
        assertFalse(Admin.isCorrectGeneratedPassword(password+"Wrong"));
    }
}
