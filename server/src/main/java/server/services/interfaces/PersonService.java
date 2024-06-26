package server.services.interfaces;

import commons.Person;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    public Person save(Person person);
    public List<Person> findAll();
    public boolean existsById(long id);
    public Optional<Person> findById(long id);
    public Person getReferenceById(long id);

    public ResponseEntity<Person> delete(Long id);
    List<Person> findPersonsByUserId(Long userId);
}
