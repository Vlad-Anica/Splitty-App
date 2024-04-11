package server.services.implementations;

import commons.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.PersonRepository;
import server.services.interfaces.PersonService;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRep;

    public PersonServiceImpl(PersonRepository personRep) {
        this.personRep = personRep;
    }

    @Override
    public Person save(Person person) {
        return personRep.save(person);
    }

    @Override
    public List<Person> findAll() {
        return personRep.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return personRep.existsById(id);
    }

    @Override
    public Optional<Person> findById(long id) {
        return personRep.findById(id);
    }

    @Override
    public Person getReferenceById(long id) {
        return personRep.getReferenceById(id);
    }

    @Override
    public ResponseEntity<Person> delete(Long id) {
        if (!personRep.existsById(id))
            return ResponseEntity.badRequest().build();
        if (findById(id).isEmpty())
            return ResponseEntity.badRequest().build();
        Person person = findById(id).get();
        personRep.delete(person);
        return ResponseEntity.ok(person);
    }
}
