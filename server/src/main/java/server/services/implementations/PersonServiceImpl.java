package server.services.implementations;

import commons.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.PersonRepository;
import server.services.interfaces.PersonService;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRep;

    public PersonServiceImpl(PersonRepository personRep) {
        this.personRep = personRep;
    }

    @Override
    public Person save(Person person) {
        personRep.save(person);
        return person;
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
    public Person findById(long id) {
        return personRep.findById(id).get();
    }
}
