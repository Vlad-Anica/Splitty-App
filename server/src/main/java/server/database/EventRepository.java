package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByInviteCode(String name);
    Optional<List<Event>>findAllByOrderByNameDesc();
    Optional<List<Event>>findAllByOrderByLastModifiedDateDesc();
    Optional<List<Event>>findAllByOrderByCreationDateDesc();
}
