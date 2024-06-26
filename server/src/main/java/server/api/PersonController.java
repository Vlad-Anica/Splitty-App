package server.api;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.PersonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private final PersonService personService;
    private DebtController debtCtrl;
    public PersonController(PersonService personService, DebtController debtCtrl) {
       this.personService = personService;
       this.debtCtrl = debtCtrl;
    }

    @GetMapping(path = { "", "/" })
    public List<Person> getAll() {
        System.out.println("Find people...");
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable long id) {

        if (id < 0 || !personService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Person> person = Optional.of(personService.findById(id).get());
        if (person.isPresent())
            return ResponseEntity.ok(person.get());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{id}/iban")
    public String getIbanById(@PathVariable("id") long id){
        if (id < 0 || !personService.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return personService.findById(id).get().getIBAN();
    }

    @GetMapping("/{id}/bic")
    public String getBicById(@PathVariable("id") long id){
        if (id < 0 || !personService.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return personService.findById(id).get().getBIC();
    }

    @GetMapping("{id}/bank")
    public String getBankById(@PathVariable("id") long id){
        if (id < 0 || !personService.existsById(id)) {
            return "Error, PERSON NOT FOUND";
        }
        return personService.findById(id).get().getIBAN() + ", " + personService.findById(id).get().getBIC();
    }

    @GetMapping("/{id}/debts")
    public List<Debt> getDebtsById(@PathVariable("id") long id){
        if (id < 0 || !personService.existsById(id)) {
            return null;
        }
        return personService.findById(id).get().getDebtList();
    }

    @GetMapping("/{id}/events")
    public Event getEventsById(@PathVariable("id") long id){
        if (id < 0 || !personService.existsById(id)) {
            return null;
        }
        return personService.findById(id).get().getEvent();
    }

    @GetMapping("/{USER_ID}/debts")
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
        System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n\n\n");
        System.out.println("Received Person object: " + person);

        if (person == null)  {

            return ResponseEntity.badRequest().build();
        }
        System.out.println("Passed the if1!!");

        if( isNullOrEmpty(person.getFirstName())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Passed the if2!!");

        if (isNullOrEmpty(person.getLastName())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Passed the all ifs!!");

        Person saved = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @RequestBody Person updatedPerson) {

        if (updatedPerson.getId() != id)
           return ResponseEntity.badRequest().build();
        if (!personService.existsById(id))
            return add(updatedPerson);
        return ResponseEntity.ok(personService.save(updatedPerson));
    }

    @PutMapping("/persist")
    public ResponseEntity<Person> persistPerson(@RequestBody Person person) {
        return ResponseEntity.ok(personService.save(person));
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
        return ResponseEntity.ok(personService.save(person));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable("id") Long id) {
        return personService.delete(id);
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
