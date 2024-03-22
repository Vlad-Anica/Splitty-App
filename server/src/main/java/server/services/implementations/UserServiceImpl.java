package server.services.implementations;

import commons.Event;
import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public User findById(long id) {
        return null;
    }
}
