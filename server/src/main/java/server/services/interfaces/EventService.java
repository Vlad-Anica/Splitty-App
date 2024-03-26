package server.services.interfaces;

import commons.Event;
import commons.Expense;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EventService {
    public Event save(Event event);
    public List<Event> findAll();
    public Optional<Event> findById(long id);
    public boolean existsById(long id);
    public Event findByInviteCode(String inviteCode);
    public void deleteById(long id);
    public ResponseEntity<List<Expense>> getExpenses(long id);
    Optional<List<Event>> getEventsOrderedByUpdatedAt();
    Optional<List<Event>> getEventsOrderedByCreatedAt();
    Optional<List<Event>> getEventsOrderedByName();

}
