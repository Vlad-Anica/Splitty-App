package server.api;

import commons.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.PersonRepository;
import server.database.UserRepository;

import java.util.*;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private final PersonRepository db;
    private DebtController debtCtrl;
    private UserRepository userRep;
    private EventRepository eventRep;

    public PersonController(PersonRepository db, DebtController debtCtrl, UserRepository userRep, EventRepository eventRep) {

        this.db = db;
        this.debtCtrl = debtCtrl;
        this.userRep = userRep;
        this.eventRep = eventRep;
    }

    /**
     * Creates a corresponding Person based on the given User ID and associated Event.
     *
     * @param uID User ID to reference
     * @param eID Event ID corresponding to the Event the person will be added to
     * @return Person object, representing the created Person
     */
    @PostMapping("/")
    public Person createPerson(@RequestParam("userID") Long uID,
                               @RequestParam("eventID") Long eID) {
        boolean badEntry = false;
        try {
            userRep.getReferenceById(uID);
        } catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no User found.");
            badEntry = true;
        }
        User user = userRep.getReferenceById(uID);
        try {
            eventRep.getReferenceById(eID);
        } catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Event found.");
            badEntry = true;
        }
        Event event = eventRep.getReferenceById(eID);
        if(badEntry) {
            return null;
        } else {
            Person person = new Person(user.getFirstName(), user.getLastName(), user.getEmail(), user.getIBAN(), user.getBIC(), user.getPreferredCurrency(), 0, event, user);
            db.save(person);
            return person;
        }
    }

    @GetMapping(path = {"", "/"})
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
    public String getIbanById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return db.findById(id).get().getIBAN();
    }

    @GetMapping("/bic/{id}")
    public String getBicById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return db.findById(id).get().getIBAN();
    }

    @GetMapping("/bank/{id}")
    public String getBankById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return db.findById(id).get().getIBAN() + ", " + db.findById(id).get().getIBAN();
    }

    @GetMapping("/{id}/debts")
    public List<Debt> getDebtsById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get().getDebtList();
    }

    @GetMapping("events/{id}")
    public Event getEventsById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get().getEvent();
    }

    @GetMapping("/{USER_ID}/debts")
    public Map<Event, Double> getTotalDebtsById(@PathVariable("USER_ID") User user) {
        List<Person> allUsers = getAll();
        Map<Event, Double> allDepts = new HashMap<>();
        for (Person p : allUsers) {
            if (p.getUser().equals(user)) {
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


    @PostMapping(path = {"", "/"})
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

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody Person updatedPerson) {

        if (updatedPerson.getId() != id)
            return ResponseEntity.badRequest().build();
        if (!db.existsById(id))
            return add(updatedPerson);
        return ResponseEntity.ok(db.save(updatedPerson));
    }

    @PutMapping("/{id}/newDebt")
    public ResponseEntity<Person> addDebt(@PathVariable Long id, @RequestBody Debt debt) {
        Person person = getById(id).getBody();
        if (person == null)
            return ResponseEntity.badRequest().build();

        debtCtrl.add(debt);
        person.getDebtList().add(debt);
        double totalDebt = person.getTotalDebt() + debt.getAmount();
        person.setTotalDebt(totalDebt);
        return ResponseEntity.ok(db.save(person));
    }

    @PutMapping("/{id}/debtSettlement/{debtId}")
    public ResponseEntity<Person> settleDebt(@PathVariable Long id, @PathVariable Long debtId) {

        Person person = getById(id).getBody();
        Debt debt = debtCtrl.getById(debtId).getBody();
        if (person == null || debt == null || !person.getDebtList().contains(debt))
            return ResponseEntity.badRequest().build();

        debt.setSettled(true);
        debtCtrl.update(debtId, debt);

        person.getDebtList().remove(debt);
        double totalDebt = person.getTotalDebt() - debt.getAmount();
        person.setTotalDebt(totalDebt >= 0 ? totalDebt : 0);

        return update(id, person);
    }

    @PutMapping("/{id}/partialPayment/{debtId}")
    public ResponseEntity<Person> payDebtPartially(@PathVariable Long id, @PathVariable Long debtId,
                                                   @RequestParam(value = "amount") double amount) {

        Person person = getById(id).getBody();
        Debt debt = debtCtrl.getById(debtId).getBody();
        if (person == null || debt == null || !person.getDebtList().contains(debt)
                || amount < 0)
            return ResponseEntity.badRequest().build();

        double totalAmount = debt.getAmount() - amount;
        debt.setAmount(totalAmount >= 0 ? totalAmount : 0);

        if (debt.getAmount() <= 0)
            return settleDebt(id, debtId);

        debtCtrl.update(debtId, debt);

        double totalDebt = person.getTotalDebt() - amount;
        person.setTotalDebt(totalDebt >= 0 ? totalDebt : 0);

        return update(id, person);

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
