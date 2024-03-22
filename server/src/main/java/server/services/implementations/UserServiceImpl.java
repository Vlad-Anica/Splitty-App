package server.services.implementations;

import commons.Event;
import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.UserRepository;
import server.services.interfaces.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRep;

    public UserServiceImpl(UserRepository userRep) {
        this.userRep = userRep;
    }


    @Override
    public List<Event> getEvents(Long userId) {
        return null;
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
    public User findById(long id) {
        return userRep.findById(id).get();
    }
}
