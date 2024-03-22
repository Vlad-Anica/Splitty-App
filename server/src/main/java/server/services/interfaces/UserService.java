package server.services.interfaces;

import commons.Event;
import commons.User;

import java.util.List;

public interface UserService {
    List<Event> getEvents(Long userId);
    public List<User> findAll();
    public User save(User user);
    public boolean existsById(long id);
    public User findById(long id);
}
