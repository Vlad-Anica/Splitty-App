package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import server.services.interfaces.EventService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;
    @Mock
    private EventService eventService;
    private Event event1;
    private Event event2;
    private Event event3;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(eventService);
        Date date1 = new Date();
        Date date2 = new Date();
        List<Tag> tags = new ArrayList<>();
        List<Person> participants = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        event1 = Event.builder().id(1L).name("c").description("1").tags(tags)
                .date(date1).participants(participants).expenses(expenses)
                .inviteCode("123").build();
        event2 = Event.builder().id(2L).name("b").description("0").tags(tags)
                .date(date2).participants(participants).expenses(expenses)
                .inviteCode("321").build();
        event3 = Event.builder().id(3L).name("a").description("3").tags(tags)
                .date(date2).participants(participants).expenses(expenses)
                .inviteCode("321").build();
        event2.setDescription("2");
    }

    @Test
    public void testCheckPassword(){
        String attempt = Admin.generateRandomPassword(new Random());
        assertTrue(adminController.checkPassword(attempt));
    }

    @Test
    void testGetEventsOrderedByLastModificationDate() {
        List<Event> events = new ArrayList<>();
        events.add(event2);
        events.add(event3);
        events.add(event1);

        when(eventService.getEventsOrderedByUpdatedAt()).thenReturn(Optional.of(events));

        List<Event> result = adminController.getEventsOrderedByLastModificationDate();
        assertEquals(3, result.size());
        assertEquals(event2, result.get(0));
        assertEquals(event3, result.get(1));
        assertEquals(event1, result.get(2));
    }

    @Test
    void testGetEventsOrderedByCreationDate() {
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);

        when(eventService.getEventsOrderedByCreatedAt()).thenReturn(Optional.of(events));

        List<Event> result = adminController.getEventsOrderedByCreationDate();
        assertEquals(3, result.size());
        assertEquals(event1, result.get(0));
        assertEquals(event2, result.get(1));
        assertEquals(event3, result.get(2));
    }

    @Test
    void testGetEventsOrderedByName() {
        List<Event> events = new ArrayList<>();
        events.add(event3);
        events.add(event2);
        events.add(event1);

        when(eventService.getEventsOrderedByName()).thenReturn(Optional.of(events));

        List<Event> result = adminController.getEventsOrderedByName();
        assertEquals(3, result.size());
        assertEquals(event3, result.get(0));
        assertEquals(event2, result.get(1));
        assertEquals(event1, result.get(2));
    }
}