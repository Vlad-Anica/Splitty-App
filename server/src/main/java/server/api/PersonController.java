package server.api;

import commons.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PersonRepository;

import java.util.List;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository db;
    public PersonController(PersonRepository db) {

       this.db = db;
    }

    @GetMapping(path = { "", "/" })
    public List<Person> getAll() {
        System.out.println("Find people...");
        return db.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable("id") long id) {

        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(db.findById(id).get());
    }

    @GetMapping("/{firstName}")
    public ResponseEntity<List<Person>> getByName(@PathVariable("firstName") String firstName,
                                                  @RequestParam("lastName") String lastName) {
        if (isNullOrEmpty(firstName) || isNullOrEmpty(lastName))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(db.findByFirstName(firstName));
    }


    @PostMapping("/")
    public ResponseEntity<Person> add(@RequestBody Person person) {

        if (person == null || isNullOrEmpty(person.getFirstName()) ||
                isNullOrEmpty(person.getLastName())
                || isNullOrEmpty(person.getIBAN())
                || isNullOrEmpty(person.getBIC())) {
            return ResponseEntity.badRequest().build();
        }

        Person saved = db.save(person);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
