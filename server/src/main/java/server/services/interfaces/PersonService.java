package server.services.interfaces;

import commons.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();
    Optional<Person> findById(long id);
    boolean existsById(long id);
    Person getReferenceById(long id);
    Person save(Person person);
    void deleteById(long id);
}
