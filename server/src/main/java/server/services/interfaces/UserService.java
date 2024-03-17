package server.services.interfaces;

import commons.Event;

import java.util.List;

public interface UserService {
    List<Event> getEvents(Long userId);
}
