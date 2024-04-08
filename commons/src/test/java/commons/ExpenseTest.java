package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    Person p = new Person("a","b");
    ArrayList<Person> persons = new ArrayList<>(List.of(p));
    List<Debt> debtList = new ArrayList<>();
    Tag t = new Tag("blue", "food");
    Date d = new Date(4000);
    Expense e = new Expense("test", 2.5, d, p, persons, Currency.EUR, t );
    Expense e2 = new Expense("test3", 2.5, d, p, persons, Currency.EUR, t );
    Expense e3 = new Expense("test", 2.5, d, p, persons, Currency.EUR, t );

    @Test
    void getDescription() {
        assertEquals("test", e.getDescription());
    }

    @Test
    void getAmount() {
        assertEquals(2.5, e.getAmount());
    }

    @Test
    void getDate() {
        assertEquals(d, e.getDate());
    }

    @Test
    void getReceiver() {
        assertEquals(p, e.getReceiver());
    }


    @Test
    void getCurrency() {
        assertEquals(Currency.EUR, e.getCurrency());
    }

    void getTag() {
        assertEquals(t, e.getTag());
    }

    @Test
    void setDescription() {
        e.setDescription("e");
        assertEquals("e",e.getDescription());

    }

    @Test
    void setAmount() {
        e.setAmount(1.6);
        assertEquals(1.6,e.getAmount());
    }

    @Test
    void setDate() {
        Date da = new Date(1,1,1,1,1,1);
        e.setDate(da);
        assertEquals(da,e.getDate());
    }

    @Test
    void setReceiver() {
        Person re = new Person("L","D");
        e.setReceiver(re);
        assertEquals(re,e.getReceiver());
    }
//
//    @Test
//    void setGivers() {
//        List<Debt> debts = new ArrayList<>();
//        e.setDebtList(debts);
//        assertEquals(debts ,e.getDebtList());
//    }

    @Test
    void setCurrency() {
        e.setCurrency(Currency.USD);
        assertEquals(Currency.USD, e.getCurrency());
    }

    @Test
    void setTag() {
        Tag d = new Tag("red", "trip");
        e.setTag(d);
        assertEquals(d, e.getTag());
    }

   // @Test
  //  void convertCurrency() {
  //      e.setCurrency(Currency.USD);
  //      assertEquals(2.725, e.getAmount());
   // }

    @Test
    void testEquals() {
        assertNotEquals(e, e2);
        assertEquals(e, e3);
    }

    @Test
    void testHashCode() {
        assertEquals(e.hashCode(), e3.hashCode());
    }

   // @Test
   // void testToString() {
   //     assertEquals(e.toString(), e3.toString());
   // }

    @Test
    void dateNotNull(){
        assertNotNull(e.getDate());
    }

    @Test
    void descriptionNotNull(){
        assertNotNull(e.getDescription());
    }

    @Test
    void receiverNotNull(){
        assertNotNull(e.getReceiver());
    }

    @Test
    void currencyNotNull(){
        assertNotNull(e.getCurrency());
    }
}