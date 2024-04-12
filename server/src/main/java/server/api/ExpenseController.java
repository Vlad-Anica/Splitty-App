package server.api;

import commons.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.DebtService;
import server.services.interfaces.ExpenseService;
import server.services.interfaces.PersonService;
import server.services.interfaces.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private final ExpenseService expenseService;
    private PersonService personService;
    private DebtService debtService;
    private TagService tagService;

    /**
     * Creates a new ExpenseController
     *
     * @param expenseService service class for Expense
     * @param personService service class for Person
     * @param debtService service class for Debt
     * @param tagService service class for Tag
     */
    public ExpenseController(ExpenseService expenseService, PersonService personService, DebtService debtService, TagService tagService) {
        this.expenseService = expenseService;
        this.personService = personService;
        this.debtService = debtService;
        this.tagService = tagService;
    }

    /***
     *
     * @param expense expense to be added
     * @return added expense
     */
    @MessageMapping("/expenses")
    @SendTo("/topic/expenses")
    public Expense addExpense(Expense expense) {

        ResponseEntity<Expense> response = add(expense);
        if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
            return null;

        return expense;
    }
    /**
     * Creates a corresponding Expense based on the given parameters. In normal usage, debtIDs should be empty...
     * @param description description to use for the Expense
     * @param amount amount spend on the Expense
     * @param date date the Expense took place
     * @param receiverID ID of the one who footed the bill
     * @param giverIDs IDs of the respective givers associated with the Expense
     * @param currency Currency the Expense was executed in
     * @param tagID Tag ID associated with the expense
     * @return the Expense object created
     */
    @PostMapping("/")
    public Expense createExpense(@RequestParam("description") String description,
                                 @RequestParam("amount") Double amount,
                                 @RequestParam("date") Date date,
                                 @RequestParam("receiver") Long receiverID,
                                 @RequestParam("givers") List<Long> giverIDs,
                                 @RequestParam("currency") Currency currency,
                                 @RequestParam("tag") Long tagID) {
        if(description == null || amount == null || date == null || currency == null) {
            System.out.println("Process aborted, null arguments received.");
            return null;
        }
        
        ArrayList<Debt> debts = new ArrayList<>();
        Expense expense = null;
        Person receiver = null;
        List<Person> givers = new ArrayList<>();

        boolean validEntry = true;
        try {
            receiver = personService.getReferenceById(receiverID);
            for (Long giverID: giverIDs) {
                givers.add(personService.getReferenceById(giverID));
            }
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Person found.");
            validEntry = false;
        }
        try {
            tagService.getReferenceById(tagID);
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Tag found.");
            validEntry = false;
        }
        if(!validEntry) {
            return null;
        }
        expense = new Expense(description, amount, date, personService.getReferenceById(receiverID), givers, currency, tagService.getReferenceById(tagID));
        expenseService.save(expense);
        return expense;
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@RequestBody Expense expense) {

        return expenseService.add(expense);
    }
    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        System.out.println("Find people...");
        return expenseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id) {

        if (id < 0 || !expenseService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseService.findById(id).get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("id") Long id, @RequestBody Expense newExpense) {

        if (newExpense.getId() != id)
            return ResponseEntity.badRequest().build();
        if (!expenseService.existsById(id))
            return add(newExpense);
        return ResponseEntity.ok(expenseService.save(newExpense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> delete(@PathVariable("id") Long id) {
        return expenseService.delete(id);
    }
//    @GetMapping("debts/{id}")
//    public List<Debt> getDebtsListById(@PathVariable("id") long id) {
//        if (id < 0 || !expenseService.existsById(id)) {
//            return null;
//        }
//        return expenseService.findById(id).get().getDebtList();
//    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

