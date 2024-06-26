package server.api;

import commons.Currency;
import commons.Debt;
import commons.Expense;
import commons.Person;
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
import server.services.implementations.TagServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {
    @InjectMocks
    private ExpenseController expenseController;
    @MockBean
    private ExpenseServiceImpl expenseService;
    @MockBean
    private PersonServiceImpl personService;
    @MockBean
    private DebtServiceImpl debtService;
    @MockBean
    private TagServiceImpl tagService;
    private Expense e1;
    private Expense e2;
    private List<Debt> debts;

    @BeforeEach
    public void setUp() {
        Person p1 = new Person();
        Person p2 = new Person();
        List<Debt> debtList = new ArrayList<>();
        MockitoAnnotations.openMocks(this);
        expenseService = mock(ExpenseServiceImpl.class);
        debtService = mock(DebtServiceImpl.class);
        personService = mock(PersonServiceImpl.class);
        tagService = mock(TagServiceImpl.class);
        expenseController = new ExpenseController(expenseService, personService, debtService, tagService);
        debts = new ArrayList<>();
        debts.add(new Debt());
        e1 = Expense.builder().id(1L).description("desc1").amount(69).date(new Date()).receiver(p1).givers(new ArrayList<>(List.of(p1))).currency(Currency.USD).build();
        e2 = Expense.builder().id(2L).description("desc2").amount(420).date(new Date()).receiver(p2).givers(new ArrayList<>()).currency(Currency.EUR).build();
    }

    @Test
    public void testGetAll() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(e1);
        expenses.add(e2);

        when(expenseService.findAll()).thenReturn(expenses);

        List<Expense> result = expenseController.getAll();
        assertEquals(2, result.size());
        assertEquals(e1, result.get(0));
        assertEquals(e2, result.get(1));
    }

    @Test
    public void testGetById() {
        when(expenseService.findById(1L)).thenReturn(Optional.ofNullable(e1));
        when(expenseService.existsById(1L)).thenReturn(true);
        when(expenseService.findById(2L)).thenReturn(Optional.ofNullable(e2));
        when(expenseService.existsById(2L)).thenReturn(true);
        assertEquals(e1, expenseController.getById(1L).getBody());
        assertEquals(e2, expenseController.getById(2L).getBody());
        assertEquals(ResponseEntity.badRequest().build(), expenseController.getById(0L));
    }

    @Test
    public void testAdd(){
        when(expenseService.add(e1)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(e1));
        when(expenseService.add(e2)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(e2));
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(e1), expenseController.add(e1));
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(e2), expenseController.add(e2));
        List<Expense> expenses = new ArrayList<>();
        expenses.add(e1);
        expenses.add(e2);
        when(expenseService.findById(1L)).thenReturn(Optional.ofNullable(e1));
        when(expenseService.existsById(1L)).thenReturn(true);
        when(expenseService.findById(2L)).thenReturn(Optional.ofNullable(e2));
        when(expenseService.existsById(2L)).thenReturn(true);
        when(expenseService.findAll()).thenReturn(expenses);
        assertEquals(2, expenseController.getAll().size());
        assertEquals(e1, expenseController.getById(1L).getBody());
        assertEquals(e2, expenseController.getById(2L).getBody());
    }
}
