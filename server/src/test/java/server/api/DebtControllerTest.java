package server.api;

import commons.Debt;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
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
public class DebtControllerTest {
    @InjectMocks
    private DebtController debtController;
    @MockBean
    private ExpenseServiceImpl expenseService;
    @MockBean
    private PersonServiceImpl personService;
    @MockBean
    private DebtServiceImpl debtService;

    private Debt d1, d2, d3;
    private Person p1, p2, p3;
    List<Debt> debtList;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        expenseService = mock(ExpenseServiceImpl.class);
        debtService = mock(DebtServiceImpl.class);
        personService = mock(PersonServiceImpl.class);
        debtController = new DebtController(debtService, personService, expenseService);
        p1 = new Person();
        p2 = new Person();
        p3 = new Person();
        d1 = new Debt(p1, p2, null,1.0);
        d2 = new Debt(p2, p3, null,2.0);
        d3 = new Debt(p3, p1, null,3.0);
        debtList = new ArrayList<>(List.of(d1, d2, d3));
    }

    @Test
    public void testGetAll() {
        when(debtService.findAll()).thenReturn(debtList);

        List<Debt> result = debtController.getAll();
        assertEquals(3, result.size());
        assertEquals(d1, result.get(0));
        assertEquals(d2, result.get(1));
        assertEquals(d3, result.get(2));
    }

    @Test
    public void testGetById() {
        when(debtService.findById(1L)).thenReturn(Optional.ofNullable(d1));
        when(debtService.existsById(1L)).thenReturn(true);
        when(debtService.findById(2L)).thenReturn(Optional.ofNullable(d2));
        when(debtService.existsById(2L)).thenReturn(true);
        assertEquals(d1, debtController.getById(1L).getBody());
        assertEquals(d2, debtController.getById(2L).getBody());
        assertEquals(ResponseEntity.badRequest().build(), debtController.getById(0L));
    }

    @Test
    public void testAdd(){
        when(debtService.save(d1)).thenReturn(d1);
        when(debtService.save(d2)).thenReturn(d2);
        assertEquals(d1, debtController.add(d1).getBody());
        assertEquals(d2, debtController.add(d2).getBody());

        when(debtService.findById(1L)).thenReturn(Optional.ofNullable(d1));
        when(debtService.existsById(1L)).thenReturn(true);
        when(debtService.findById(2L)).thenReturn(Optional.ofNullable(d2));
        when(debtService.existsById(2L)).thenReturn(true);
        when(debtService.findById(3L)).thenReturn(Optional.ofNullable(d3));
        when(debtService.existsById(3L)).thenReturn(true);
        when(debtService.findAll()).thenReturn(debtList);


        assertEquals(3, debtController.getAll().size());
        assertEquals(d1, debtController.getById(1L).getBody());
        assertEquals(d2, debtController.getById(2L).getBody());
        assertEquals(d3, debtController.getById(3L).getBody());
    }

    @Test
    public void testUpdate() {
        assertEquals(d1, debtController.update(1L, d1).getBody());
    }

}
