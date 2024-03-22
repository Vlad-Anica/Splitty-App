package server.api;

import commons.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import server.database.PersonRepository;
import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;
//import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private final ExpenseRepository expenseRep;
    private PersonRepository personRep;
    private DebtRepository debtRep;

    /**
     * Creates a new ExpenseController
     *
     * @param expenseRep repository for Expense
     * @param personRep  repository for Person
     * @param debtRep    repository for Debt
     */
    public ExpenseController(ExpenseRepository expenseRep, PersonRepository personRep, DebtRepository debtRep) {
        this.expenseRep = expenseRep;
        this.personRep = personRep;
        this.debtRep = debtRep;
    }

    /**
     * Creates a corresponding Expense based on the given parameters. In normal usage, debtIDs should be empty...
     *
     * @param description description to use for the Expense
     * @param amount      amount spend on the Expense
     * @param date        date the Expense took place
     * @param receiverID  ID of the one who footed the bill
     * @param debtIDs     IDs of the respective debts associated with the Expense
     * @param currency    Currency the Expense was executed in
     * @param tag         Tag associated with the expense
     * @return the Expense object created
     */
    @PostMapping("/")
    public Expense createExpense(@RequestParam("description") String description,
                                 @RequestParam("amount") Double amount,
                                 @RequestParam("date") Date date,
                                 @RequestParam("receiver") Long receiverID,
                                 @RequestParam("debts") List<Long> debtIDs,
                                 @RequestParam("currency") Currency currency,
                                 @RequestParam("tag") Tag tag) {
        if (description == null || amount == null || date == null || currency == null || tag == null) {
            System.out.println("Process aborted, null arguments received.");
            return null;
        }
        boolean badEntry = false;
        ArrayList<Debt> debts = new ArrayList<>();
        try {
            personRep.getReferenceById(receiverID);
        } catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Person found.");
            badEntry = true;
        }
        try {
            for (Long dID : debtIDs) {
                debtRep.getReferenceById(dID);
                debts.add(debtRep.getReferenceById(dID));
            }
        } catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Debt found.");
            badEntry = true;
        }
        if(badEntry) {
            return null;
        } else {
            Expense expense = new Expense(description, amount, date, personRep.getReferenceById(receiverID), debts, currency, tag);
            expenseRep.save(expense);
            return expense;
        }
    }

    @GetMapping(path = {"", "/"})
    public List<Expense> getAll() {
        System.out.println("Find people...");
        return expenseRep.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id) {

        if (id < 0 || !expenseRep.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(expenseRep.findById(id).get());
    }


    @GetMapping("participants/{id}")
    public List<Debt> getParticipantsById(@PathVariable("id") long id) {
        if (id < 0 || !expenseRep.existsById(id)) {
            return null;
        }
        return expenseRep.findById(id).get().getDebtList();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

