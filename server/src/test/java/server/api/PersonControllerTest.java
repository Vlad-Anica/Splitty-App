package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.implementations.DebtServiceImpl;
import server.services.implementations.ExpenseServiceImpl;
import server.services.implementations.PersonServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {
    @InjectMocks
    private PersonController personController;
    @InjectMocks
    DebtController debtController;
    @MockBean
    private PersonServiceImpl personService;
    @MockBean
    private DebtServiceImpl debtService;
    @MockBean
    private ExpenseServiceImpl expenseService;
    private Person person1;
    private Person person2;
    @BeforeEach
    public void setUp() {
        Event event = new Event();
        User user = new User();
        List<Debt> debtList = new ArrayList<>();
        MockitoAnnotations.openMocks(this);
        expenseService = mock(ExpenseServiceImpl.class);
        debtService = mock(DebtServiceImpl.class);
        personService = mock(PersonServiceImpl.class);
        debtController = new DebtController(debtService, personService, expenseService);
        personController = new PersonController(personService, debtController);
        person1 = Person.builder().id(1L).firstName("Frank").lastName("Verkoren")
                .email("frank@gmail.com").IBAN("INGB1234567890").BIC("1234567")
                .preferredCurrency(Currency.EUR).totalDebt(20D).event(event)
                .user(user).debtList(debtList).build();
        person2 = Person.builder().id(2L).firstName("Duco").lastName("Lam")
                .email("duco@gmail.com").IBAN("INGB12345678901").BIC("1235674")
                .preferredCurrency(Currency.EUR).totalDebt(30D).event(event)
                .user(user).debtList(debtList).build();
    }
    @Test
    public void testGetAll() {
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        when(personService.findAll()).thenReturn(persons);

        List<Person> result = personController.getAll();
        assertEquals(2, result.size());
        assertEquals(person1, result.get(0));
        assertEquals(person2, result.get(1));
    }
    @Test
    public void testGetById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1, personController.getById(1L).getBody());
        assertEquals(person2, personController.getById(2L).getBody());
    }
    @Test
    public void testGetIbanById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1.getIBAN(), personController.getIbanById(1L));
        assertEquals(person2.getIBAN(), personController.getIbanById(2L));
    }

    @Test
    public void testGetBicById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1.getBIC(), personController.getBicById(1L));
        assertEquals(person2.getBIC(), personController.getBicById(2L));
    }
    @Test
    public void testGetBankById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1.getIBAN() + ", " + person1.getBIC(), personController.getBankById(1L));
        assertEquals(person2.getIBAN() + ", " + person2.getBIC(), personController.getBankById(2L));
    }
    @Test
    public void testGetDebtsById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1.getDebtList(), personController.getDebtsById(1L));
        assertEquals(person2.getDebtList(), personController.getDebtsById(2L));
    }
    @Test
    public void testGetEventsById(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(person1.getEvent(), personController.getEventsById(1L));
        assertEquals(person2.getEvent(), personController.getEventsById(2L));
    }
    @Test
    public void testAdd(){
        when(personService.save(person1)).thenReturn(person1);
        when(personService.save(person2)).thenReturn(person2);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(person1), personController.add(person1));
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(person2), personController.add(person2));
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.findById(2L)).thenReturn(Optional.ofNullable(person2));
        when(personService.existsById(2L)).thenReturn(true);
        when(personService.findAll()).thenReturn(persons);
        assertEquals(2, personController.getAll().size());
        assertEquals(person1, personController.getById(1L).getBody());
        assertEquals(person2, personController.getById(2L).getBody());
    }
    @Test
    public void testUpdate(){
        Event event = new Event();
        User user = new User();
        List<Debt> debtList = new ArrayList<>();
        Person newPerson =  Person.builder().id(1L).firstName("Frank").lastName("Verkoren")
                .email("frank@gmail.cm").IBAN("INGB1234567890").BIC("1234567")
                .preferredCurrency(Currency.EUR).totalDebt(20D).event(event)
                .user(user).debtList(debtList).build();
        when(personService.save(person1)).thenReturn(person1);
        when(personService.save(person2)).thenReturn(person2);
        when(personService.save(newPerson)).thenReturn(newPerson);
        when(personService.existsById(1L)).thenReturn(true);
        when(personService.existsById(2L)).thenReturn(true);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(person1), personController.update(1L, person1));
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(person2), personController.update(2L, person2));
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(newPerson), personController.update(1L, newPerson));
        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(newPerson);
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.findById(2L)).thenReturn(Optional.of(newPerson));
        when(personService.findAll()).thenReturn(persons);
        assertEquals(2, personController.getAll().size());
        assertEquals(person1, personController.getById(1L).getBody());
        assertEquals(newPerson, personController.getById(2L).getBody());
    }
    @Test
    public void testAddDebt(){
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        Debt debt = new Debt(person1, person2, null, 20);
        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(debt);
        when(debtService.save(debt)).thenReturn(debt);
        when(personService.save(person1)).thenReturn(person1);
        assertEquals(debts, Objects.requireNonNull(personController.addDebt(1L, debt).getBody()).getDebtList());
    }
    @Test
    public void testSettleDebt(){
        Debt debt = new Debt(person1, person2, null, 20);
        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(debt);
        person1.addDebt(debt);
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(debtService.existsById(0L)).thenReturn(true);
        when(debtService.findById(0L)).thenReturn(Optional.of(debt));
        when(personService.save(person1)).thenReturn(person1);
        debts.remove(debt);
        assertEquals(debts, Objects.requireNonNull(personController.settleDebt(1L, 0L).getBody()).getDebtList());
    }
    @Test
    public void testPayDebtPartially(){
        Debt debt = new Debt(person1, person2, null, 20);
        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(debt);
        person1.addDebt(debt);
        when(personService.findById(1L)).thenReturn(Optional.ofNullable(person1));
        when(personService.existsById(1L)).thenReturn(true);
        when(debtService.existsById(0L)).thenReturn(true);
        when(debtService.findById(0L)).thenReturn(Optional.of(debt));
        when(personService.save(person1)).thenReturn(person1);
        assertEquals(5, personController.payDebtPartially(1L, 0L, 15).getBody().getDebtList().getFirst().getAmount());
    }
}
