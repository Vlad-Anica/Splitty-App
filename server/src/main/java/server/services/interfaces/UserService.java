package server.services.interfaces;

import commons.Event;
import commons.User;

import java.util.List;

public interface UserService {
    List<Event> getEvents(Long userId);
}
