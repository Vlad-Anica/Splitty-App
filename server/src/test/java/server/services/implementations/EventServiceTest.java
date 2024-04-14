package server.services.implementations;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;
import server.services.interfaces.EventService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    private EventService eventService;
    @Mock
    private EventRepository repo;
    @Mock
    private PersonRepository personRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    private Event event1;
    private Event event2;
    private List<Event> events;

    @BeforeEach
    public void setup(){
        eventService = new EventServiceImpl(repo, personRepo, expenseRepo);

        Date created1 = new Date(2024, 3, 30, 16,50);
        Date created2 = new Date(2024, 3, 30, 16,54);
        Date updated1 = new Date(2024, 3, 30, 16,55);
        Date updated2 = new Date(2024, 3, 30, 16,58);
        Date date1 = new Date(2024, 4, 1, 16,58);
        Date date2 = new Date(2024, 4, 2, 16,58);
        List<Tag> tags1 = new ArrayList<>();
        List<Person> participants = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        List<Debt> debts = new ArrayList<>();


        event1 = Event.builder().id(1L).createdAt(created1).updatedAt(updated1).name("a")
                .description("aa").tags(tags1).date(date1).participants(participants).expenses(expenses)
                .debts(debts).inviteCode("123").build();

        event2 = Event.builder().id(2L).createdAt(created2).updatedAt(updated2).name("b")
                .description("bb").tags(tags1).date(date2).participants(participants).expenses(expenses)
                .debts(debts).inviteCode("456").build();

        events = new ArrayList<>();
    }

    @Test
    void saveTest(){
        when(repo.save(event1)).thenReturn(event1);
        Event result = eventService.save(event1);
        assertEquals(event1, result);
    }

    @Test
    void findAllTestFull(){
        events.add(event1);
        events.add(event2);

        when(repo.findAll()).thenReturn(events);
        List<Event> result = eventService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void findAllTestEmpty(){
        when(repo.findAll()).thenReturn(events);
        List<Event> result = eventService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void existByIdTest(){
        events.add(event1);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        boolean doesExist = eventService.existsById(1L);
        boolean doesNotExist = eventService.existsById(2L);
        assertTrue(doesExist);
        assertFalse(doesNotExist);
    }

    @Test
    void existByIdTestNegative(){
        when(repo.existsById(-1L)).thenReturn(false);
        boolean doesNotExist = eventService.existsById(-1L);
        assertFalse(doesNotExist);
    }

    @Test
    public void testFindByID() {
        when(repo.findById(1L)).thenReturn(Optional.of(event1));
        assertEquals(event1, eventService.findById(1L).get());
    }
    @Test
    public void testFindByInviteCode() {
        when(repo.findByInviteCode("123")).thenReturn(event1);
        assertEquals(event1, eventService.findByInviteCode("123"));
    }

    @Test
    void testGetEventsOrderedByUpdatedAt(){
        events.add(event2);
        events.add(event1);

        when(repo.findAllByOrderByUpdatedAtDesc()).thenReturn(Optional.of(events));
        List<Event> result = eventService.getEventsOrderedByUpdatedAt().get();
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
    }

    @Test
    void testGetEventsOrderedByCreatedAt(){
        events.add(event1);
        events.add(event2);

        when(repo.findAllByOrderByCreatedAtDesc()).thenReturn(Optional.of(events));
        List<Event> result = eventService.getEventsOrderedByCreatedAt().get();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetEventsOrderedByName(){
        events.add(event1);
        events.add(event2);

        when(repo.findAllByOrderByNameDesc()).thenReturn(Optional.of(events));
        List<Event> result = eventService.getEventsOrderedByName().get();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

}