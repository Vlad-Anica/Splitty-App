package server.services.interfaces;

import commons.Event;
import commons.Person;
import commons.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Event> getEvents(Long userId);
    List<User> findAll();
    Optional<User> findById(long id);
    boolean existsById(long id);
    User getReferenceById(long id);
    User save(User user);
    void deleteById(long id);
}
