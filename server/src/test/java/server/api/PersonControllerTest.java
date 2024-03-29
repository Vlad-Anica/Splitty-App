package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.services.implementations.DebtServiceImpl;
import server.services.implementations.ExpenseServiceImpl;
import server.services.implementations.PersonServiceImpl;

import java.util.ArrayList;
import java.util.List;
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
}
