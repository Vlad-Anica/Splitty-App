package server.services.implementations;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.UserRepository;
import server.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRep;
    @Autowired
    private EventRepository eventRep;

    public UserServiceImpl(UserRepository userRep, EventRepository eventRep) {
        this.userRep = userRep;
        this.eventRep = eventRep;
    }
    @Override
    public ResponseEntity<List<Event>> getEvents(Long userId) {

        if (userId < 0 || !userRep.existsById(userId))
            return ResponseEntity.badRequest().build();
        List<Event> result = new ArrayList<>();
        List<Event> events = eventRep.findAll();
        for (Event event : events)
        {
            System.out.println(event.getParticipants().size());
            for (Person p : event.getParticipants())
                if (p.getUser().getId() == userId && !result.contains(event))
                    result.add(event);

        }

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<List<Expense>> getExpenses(long id) {
        if (id < 0 || !userRep.existsById(id))
            return ResponseEntity.badRequest().build();

        if (!findById(id).hasBody())
            return ResponseEntity.badRequest().build();

        User user = findById(id).getBody();

        List<Event> events = getEvents(id).getBody();
        if (events == null)
            return ResponseEntity.notFound().build();
        List<Expense> result = new ArrayList<>();
        for (Event event : events) {
            for (Expense expense : event.getExpenses())
            {
                for (Person p : expense.getInvolved())
                    if (user.getParticipants().contains(p) &&
                            !result.contains(expense))
                        result.add(expense);
            }
        }

        return ResponseEntity.ok(result);
    }
    @Override
    public List<User> findAll() {
        return userRep.findAll();
    }

    @Override
    public ResponseEntity<User> save(User user) {
        if(user.getFirstName() == null || user.getLastName() == null || user.getBIC() == null || user.getParticipants() == null
                || user.getEmail() == null || user.getPreferredCurrency() == null || user.getIBAN() == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userRep.save(user));
    }

    @Override
    public boolean existsById(long id) {
        return userRep.existsById(id);
    }

    @Override
    public ResponseEntity<User> findById(long id) {
        if (id < 0 || !userRep.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> user = userRep.findById(id);
        if (user.isPresent())
            return ResponseEntity.ok(user.get());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
