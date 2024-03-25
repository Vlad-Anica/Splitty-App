package server.services.implementations;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.services.interfaces.EventService;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    EventRepository eventRep;

    public EventServiceImpl(EventRepository eventRep) {
        this.eventRep = eventRep;
    }

    @Override
    public Event save(Event event) {
        return eventRep.save(event);
    }

    @Override
    public List<Event> findAll() {
        return eventRep.findAll();
    }

    @Override
    public Optional<Event> findById(long id) {
        return eventRep.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return eventRep.existsById(id);
    }

    @Override
    public Event findByInviteCode(String inviteCode) {
        return eventRep.findByInviteCode(inviteCode);
    }

    @Override
    public void deleteById(long id) {
        eventRep.deleteById(id);
    }
}
