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

//    @Test
//    void testHashCode() {
//    }

    @Test
    void testToString() {
        Date d1 = new Date(123,1,1,2024,24,24);
        String testResult = d1.toString();
        String result = "commons.Date@7c2a69b4[\n" +
                "  day=1\n" +
                "  hour=24\n" +
                "  id=123\n" +
                "  minute=24\n" +
                "  month=1\n" +
                "  year=2024\n" +
                "]";
        System.out.println(testResult.substring(21));
        System.out.println(result.substring(21));
        assertTrue(testResult.substring(21).equals(result.substring(21)));
    }
}