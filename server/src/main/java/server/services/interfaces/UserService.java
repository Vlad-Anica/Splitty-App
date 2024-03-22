package server.services.interfaces;

import commons.Event;

import java.util.List;

public interface UserService {
    List<Event> getEvents(Long userId);
    public List<Event> findAll();
    public Event save(Event event);
    public boolean existsById(long id);
    public Event findById(long id);
}
