package server.api;

import commons.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PersonRepository;

import java.util.List;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
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


    @GetMapping("/iban/{id}")
    public String getIbanById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        ResponseEntity<Person> p = getById(id);
        return db.findById(id).get().getIBAN();
    }

    @GetMapping("/bic/{id}")
    public String getBicById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return db.findById(id).get().getIBAN();
    }

    @GetMapping("/bank/{id}")
    public String getBankById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return db.findById(id).get().getIBAN() + ", " + db.findById(id).get().getIBAN();
    }

//    @GetMapping("/{firstName}")
//    public ResponseEntity<List<Person>> getByName(@PathVariable("firstName") String firstName,
//                                                  @RequestParam("lastName") String lastName) {
//        if (isNullOrEmpty(firstName) || isNullOrEmpty(lastName))
//            return ResponseEntity.badRequest().build();
//
//        return ResponseEntity.ok(db.findByFirstName(firstName));
//    }


    @PostMapping(path = { "", "/" })
    public ResponseEntity<Person> add(@RequestBody Person person) {
        System.out.println("Received Person object: " + person);

        if (person == null || isNullOrEmpty(person.getFirstName()) ||
                isNullOrEmpty(person.getLastName())
                || isNullOrEmpty(person.getIBAN())
                || isNullOrEmpty(person.getBIC())) {
            return ResponseEntity.badRequest().build();
        }

        Person saved = db.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
