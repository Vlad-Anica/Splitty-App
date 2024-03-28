package server.api;

import commons.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /**
     * Creates a corresponding Expense based on the given parameters. In normal usage, debtIDs should be empty...
     * @param description description to use for the Expense
     * @param amount amount spend on the Expense
     * @param date date the Expense took place
     * @param receiverID ID of the one who footed the bill
     * @param debtIDs IDs of the respective debts associated with the Expense
     * @param currency Currency the Expense was executed in
     * @param tagID Tag ID associated with the expense
     * @return the Expense object created
     */
    @PostMapping("/")
    public Expense createExpense(@RequestParam("description") String description,
                                 @RequestParam("amount") Double amount,
                                 @RequestParam("date") Date date,
                                 @RequestParam("receiver") Long receiverID,
                                 @RequestParam("debts") List<Long> debtIDs,
                                 @RequestParam("currency") Currency currency,
                                 @RequestParam("tag") Long tagID) {
        if(description == null || amount == null || date == null || currency == null) {
            System.out.println("Process aborted, null arguments received.");
            return null;
        }
        ArrayList<Debt> debts = new ArrayList<>();
        Expense expense = null;
        boolean validEntry = true;
        try {
            personService.getReferenceById(receiverID);
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Person found.");
            validEntry = false;
        }
        try {
            for (Long dID : debtIDs) {
                debtService.getReferenceById(dID);
                debts.add(debtService.getReferenceById(dID));
            }
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Debt found for an entry.");
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
        expense = new Expense(description, amount, date, personService.getReferenceById(receiverID), debts, currency, tagService.getReferenceById(tagID));
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


    @GetMapping("participants/{id}")
    public List<Debt> getParticipantsById(@PathVariable("id") long id) {
        if (id < 0 || !expenseService.existsById(id)) {
            return null;
        }
        return expenseService.findById(id).get().getDebtList();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

