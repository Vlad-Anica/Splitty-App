package server.services.implementations;

import commons.Event;
import commons.User;
import org.springframework.stereotype.Service;
import server.services.interfaces.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Override
    public List<Event> getEvents(Long userId) {
        Event e1 = new Event();
        e1.setName("event1");
        Event e2 = new Event();
        e1.setName("event2");
        Event e3 = new Event();
        e1.setName("event3");

        return List.of(e1, e2, e3);
    }
}
