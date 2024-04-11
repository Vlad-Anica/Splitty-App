package server.services.implementations;

import commons.Debt;
import commons.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.DebtRepository;
import server.services.interfaces.DebtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class DebtServiceTest {
    private DebtService debtService;
    @MockBean
    private DebtRepository debtRep;

    private Debt d1, d2, d3;
    private Person p1, p2, p3;
    List<Debt> debtList;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        debtRep = mock(DebtRepository.class);
        debtService = new DebtServiceImpl(debtRep);
        p1 = new Person();
        p2 = new Person();
        p3 = new Person();
        d1 = new Debt(p1, p2, null,1.0);
        d2 = new Debt(p2, p3, null,2.0);
        d3 = new Debt(p3, p1, null,3.0);
        debtList = new ArrayList<>(List.of(d1, d2, d3));
    }

    @Test
    public void testFindAll() {
        when(debtRep.findAll()).thenReturn(debtList);

        List<Debt> result = debtService.findAll();
        assertEquals(result, debtList);
    }

    @Test
    public void testFindByID() {
        when(debtRep.findById(1L)).thenReturn(Optional.of(d1));
        assertEquals(d1, debtService.findById(1L).get());
    }

    @Test
    public void testSave() {
        when(debtRep.save(d1)).thenReturn(d1);
        assertEquals(d1, debtService.save(d1));
    }

    @Test
    public void testExistsById() {
        when(debtRep.existsById(1L)).thenReturn(true);
        assertTrue(debtService.existsById(1L));
    }

    @Test
    public void testGetReferenceById() {
        when(debtRep.getReferenceById(1L)).thenReturn(d1);
        assertEquals(d1, debtService.getReferenceById(1L));
    }


}