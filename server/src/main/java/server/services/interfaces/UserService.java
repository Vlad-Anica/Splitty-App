package server.services.interfaces;

import commons.Event;
import commons.Expense;
import commons.User;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface UserService {
    ResponseEntity<List<Event>> getEvents(Long userId);
    public List<User> findAll();
    public ResponseEntity<User> save(User user);
    public boolean existsById(long id);
    public ResponseEntity<User> findById(long id);
    ResponseEntity<List<Expense>> getExpenses(long id);
}
