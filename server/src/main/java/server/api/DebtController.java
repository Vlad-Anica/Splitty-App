package server.api;

import commons.Debt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.implementations.DebtServiceImpl;
import server.services.implementations.ExpenseServiceImpl;
import server.services.implementations.PersonServiceImpl;
import server.services.interfaces.DebtService;
import server.services.interfaces.ExpenseService;
import server.services.interfaces.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private DebtService debtService;
    private PersonService personService;
    private ExpenseService expenseService;

    /**
     * constructor for DebtController
     * @param debtService service class for debts
     * @param personService service class for debts
     * @param expenseService service class for debts
     */
    public DebtController(DebtServiceImpl debtService, PersonServiceImpl personService, ExpenseServiceImpl expenseService) {
        this.debtService = debtService;
        this.personService = personService;
        this.expenseService = expenseService;
    }

    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return debtService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        if (id < 0 || !debtService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(debtService.findById(id));
    }
    /**
     * endpoint for creating a debt
     * @param debt the given debt to add to the database
     * @return the debt in json format
     */
    @PostMapping(path = { "", "/" })
    public  ResponseEntity<Debt> add(@RequestBody Debt debt) {
        if (debt.getGiver() == null || debt.getReceiver() == null || debt.getAmount() < 0)
            return ResponseEntity.badRequest().build();
        Debt saved = debtService.save(debt);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * endpoint for creating a user
     * @param giverId id of the giver
     * @param receiverId id of receiver
     * @param expenseId id of expense
     * @param amount amount to be paid
     * @return a Debt
     */
    @PostMapping("/")
    public Debt createDebt(@RequestParam("giver") Long giverId,
                           @RequestParam("receiver") Long receiverId, @RequestParam("expense") Long expenseId,
                           @RequestParam("amount") Double amount) {
        Debt debt = new Debt(personService.getReferenceById(giverId), personService.getReferenceById(receiverId),
                expenseService.getReferenceById(expenseId), amount);
        debtService.save(debt);
        return debt;
    }
    /**
    * endpoint for udating debt
    * @param id id of debt to update
     * @param updatedDebt debt with updated properties
     * @return updated debt
     */
    @PutMapping("/{id}")
    public ResponseEntity<Debt> update(@PathVariable("id") long id,
                                       @RequestBody Debt updatedDebt) {

        return ResponseEntity.ok(updatedDebt);
    }
}
