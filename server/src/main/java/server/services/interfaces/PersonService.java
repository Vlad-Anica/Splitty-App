package server.services.interfaces;

import commons.Person;

import java.util.List;

public interface PersonService {
    public Person save(Person person);
    public List<Person> findAll();
    public boolean existsById(long id);
    public Person findById(long id);
}
