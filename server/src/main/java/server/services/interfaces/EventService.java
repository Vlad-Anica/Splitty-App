package server.services.interfaces;

import commons.Event;

import java.util.List;

public interface EventService {
    public Event save(Event event);
    public List<Event> findAll();
    public Event findById(long id);
    public boolean existsById(long id);
    public Event findByInviteCode(String inviteCode);
    public void deleteById(long id);
}
