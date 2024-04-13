package server.database;

import commons.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstName(String firstName);

    Optional<List<Person>> findAllByOrderByLastVisitedDesc();

    List<Person> findPersonsByUserId(Long userId);
}
