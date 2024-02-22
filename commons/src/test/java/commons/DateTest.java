package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {

    @Test
    void testInit1(){
        Date d = new Date(123,1,1,2024,24,24);
        System.out.println(d.toString());
        assertNotNull(d);
    }

    @Test
    void testEquals1() {
        Date d1 = new Date(123,1,1,2024,24,24);
        Date d2 = new Date(123,1,1,2024,24,24);
        assertEquals(d1, d2);
    }

    @Test
    void testEquals2() {
        Date d1 = new Date(123,1,1,2024,24,24);
        Date d2 = new Date(124,1,1,2024,24,24);
        assertNotEquals(d1, d2);
    }

    @Test
    void testHashCode1() {
        Date d1 = new Date(123,1,1,2024,24,24);
        Date d2 = new Date(123,1,1,2024,24,24);
        assertTrue(d1.hashCode() == d2.hashCode());
    }

    @Test
    void testHashCode2() {
        Date d1 = new Date(123,1,1,2024,24,24);
        Date d2 = new Date(124,1,1,2024,24,24);
        assertFalse(d1.hashCode() == d2.hashCode());
    }
}