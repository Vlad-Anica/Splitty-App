package server.services.interfaces;

import commons.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event save(Event event);
    List<Event> findAll();
    Optional<Event> findById(long id);
    boolean existsById(long id);
    Event findByInviteCode(String inviteCode);
    void deleteById(long id);

    /**
     *
     * @return all events ordered by their name in descending order
     */
    Optional<List<Event>> getEventsOrderedByName();

    /**
     *
     * @return all events ordered by their creation date in descending order
     */
    Optional<List<Event>> getEventsOrderedByCreationDate();

    /**
     *
     * @return all events ordered by their date when they were last modified in descending order
     */
    Optional<List<Event>> getEventsOrderedByLastModifiedDate();

}
