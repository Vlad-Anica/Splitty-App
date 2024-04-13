package server.services.implementations;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.database.ExpenseRepository;
import server.services.interfaces.ExpenseService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExpenseServiceTest {

    private ExpenseService expenseService;
    @Mock
    private ExpenseRepository repo;
    private Expense expense1;
    private Expense expense2;
    private List<Expense> expenses;

    @BeforeEach
    public void setup(){
        expenseService = new ExpenseServiceImpl(repo);

        Date created1 = new Date(2024, 3, 30, 16,50);
        Date created2 = new Date(2024, 3, 30, 16,54);
        Date updated1 = new Date(2024, 3, 30, 16,55);
        Date updated2 = new Date(2024, 3, 30, 16,58);
        Date date1 = new Date(2024, 4, 1, 16,58);
        Date date2 = new Date(2024, 4, 2, 16,58);
        List<Person> givers = new ArrayList<>();
        Tag tag1 = new Tag("green", "Food");
        Person receiver = new Person("John", "Doe");



        expense1 = Expense.builder().id(1L).createdAt(created1).updatedAt(updated1)
                .description("aa").amount(3.5).date(date1).receiver(receiver).givers(givers).currency(Currency.EUR)
                .tag(tag1).build();

        expense2 = Expense.builder().id(2L).createdAt(created2).updatedAt(updated2)
                .description("bb").amount(3.5).date(date1).receiver(receiver).givers(givers).currency(Currency.USD)
                .tag(tag1).build();

        expenses = new ArrayList<>();
    }

    @Test
    void saveTest(){
        when(repo.save(expense1)).thenReturn(expense1);
        Expense result = expenseService.save(expense1);
        assertEquals(expense1, result);
    }

    @Test
    void findAllTestFull(){
        expenses.add(expense1);
        expenses.add(expense2);

        when(repo.findAll()).thenReturn(expenses);
        List<Expense> result = expenseService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void findAllTestEmpty(){
        when(repo.findAll()).thenReturn(expenses);
        List<Expense> result = expenseService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void existByIdTest(){
        expenses.add(expense1);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        boolean doesExist = expenseService.existsById(1L);
        boolean doesNotExist = expenseService.existsById(2L);
        assertTrue(doesExist);
        assertFalse(doesNotExist);
    }

    @Test
    void existByIdTestNegative(){
        when(repo.existsById(-1L)).thenReturn(false);
        boolean doesNotExist = expenseService.existsById(-1L);
        assertFalse(doesNotExist);
    }

    @Test
    public void testFindByID() {
        when(repo.findById(1L)).thenReturn(Optional.of(expense1));
        assertEquals(expense1, expenseService.findById(1L).get());
    }
    @Test
    public void testGetReferenceById() {
        when(repo.getReferenceById(1L)).thenReturn(expense1);
        assertEquals(expense1, expenseService.getReferenceById(1L));
    }

}