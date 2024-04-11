package server.services.implementations;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.PersonRepository;
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
    @Autowired
    private PersonRepository personRep;

    public UserServiceImpl(UserRepository userRep, EventRepository eventRep, PersonRepository personRep) {
        this.userRep = userRep;
        this.eventRep = eventRep;
        this.personRep = personRep;
    }
    @Override
    public ResponseEntity<List<Event>> getEvents(Long userId) {

        if (userId < 0 || !userRep.existsById(userId))
            return ResponseEntity.badRequest().build();
        List<Event> result = new ArrayList<>();
        List<Person> persons = personRep.findAllByOrderByLastVisitedDesc().get();

        for (Person person: persons)
        {
            System.out.println(person.getLastVisited());
            if (person.getUser().getId() == userId) {
                result.add(eventRep.getReferenceById(person.getEvent().getId()));
            }
        }
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<List<Expense>> getExpenses(long id) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();
        List<Event> events = getEvents(id).getBody();
        if (events == null || events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Expense> result = new ArrayList<>();
        for (Event event : events) {
            for (Expense expense : event.getExpenses()) {
                if (expense.getInvolved().stream().anyMatch(p -> p.getUser().equals(user))) {
                    result.add(expense);
                }
            }
        }

        return ResponseEntity.ok(result);
    }
    @Override
    public List<User> findAll() {
        return userRep.findAll();
    }

    @Override
    public User save(User user) {
        if(user.getFirstName() == null || user.getLastName() == null)
            return null;

     return userRep.save(user);
    }

    @Override
    public boolean existsById(long id) {
        return userRep.existsById(id);
    }

    @Override
    public Optional<User> findById(long id) {
        if (id < 0 || !userRep.existsById(id)) {
            return Optional.empty();
        }

        Optional<User> user = userRep.findById(id);
        if (user.isPresent())
            return user;
        return userRep.findById(id);
    }
}
