package server.services.implementations;

import commons.Event;
import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.UserRepository;
import server.services.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRep;

    public UserServiceImpl(UserRepository userRep) {
        this.userRep = userRep;
    }
    @Override
    public List<Event> getEvents(Long userId) {
        Event e1 = new Event();
        e1.setName("event1");
        Event e2 = new Event();
        e2.setName("event2");
        Event e3 = new Event();
        e3.setName("event3");

        return List.of(e1, e2, e3);
    }

    @Override
    public List<User> findAll() {
        return userRep.findAll();
    }

    @Override
    public User save(User user) {
        userRep.save(user);
        return user;
    }

    @Override
    public boolean existsById(long id) {
        return userRep.existsById(id);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRep.findById(id);
    }
}
