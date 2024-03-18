package server.api;

import commons.Debt;
import commons.Event;
import commons.Person;
import commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.PersonRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;


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
    public ResponseEntity<Person> getById(@PathVariable long id) {

        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Person> person = db.findById(id);
        if (person.isPresent())
            return ResponseEntity.ok(person.get());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/iban/{id}")
    public String getIbanById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
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

    @GetMapping("debts/{id}")
    public List<Debt> getDebtsById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get().getDebtList();
    }

    @GetMapping("events/{id}")
    public Event getEventsById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get().getEvent();
    }

    @GetMapping("/debts/{USER_ID}")
    public Map<Event, Double> getTotalDebtsById(@PathVariable("USER_ID") User user){
        List<Person> allUsers = getAll();
        Map<Event, Double> allDepts = new HashMap<>();
        for (Person p : allUsers){
            if (p.getUser().equals(user)){
                Event event = p.getEvent();
                Double eventDept = p.getTotalDebt();
                allDepts.put(event, eventDept);
            }
        }
        return allDepts;
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
