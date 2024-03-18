package server.api;

import commons.Debt;
import commons.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;
@RestController
@RequestMapping("/debts")
public class DebtController {
    private DebtRepository debtRep;
    private PersonRepository personRep;
    private ExpenseRepository expenseRep;

    /**
     * constructor for DebtController
     * @param debtRep repository for debts
     * @param personRep repository for debts
     * @param expenseRep repository for debts
     */
    public DebtController(DebtRepository debtRep, PersonRepository personRep, ExpenseRepository expenseRep) {
        this.debtRep = debtRep;
        this.personRep = personRep;
        this.expenseRep = expenseRep;
    }

    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return debtRep.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        if (id < 0 || !debtRep.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(debtRep.findById(id).get());
    }
    /**
     * endpoint for creating a debt
     * @param giverId id of the giver
     * @param receiverId id of receiver
     * @param expenseId id of expense
     * @param amount amount to be paid
     * @return a Debt
     */
    @PostMapping("/")
    public  ResponseEntity<Debt> createDebt(@RequestParam("giver") Long giverId,
                           @RequestParam("receiver") Long receiverId, @RequestParam("expense") Long expenseId,
                           @RequestParam("amount") Double amount) {
        if (giverId == null || receiverId == null || amount < 0)
            return ResponseEntity.badRequest().build();
        Debt debt = new Debt(personRep.getReferenceById(giverId), personRep.getReferenceById(receiverId),
                expenseRep.getReferenceById(expenseId), amount);
        debtRep.save(debt);
        return ResponseEntity.status(HttpStatus.CREATED).body(debt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Debt> update(@PathVariable("id") long id,
                                       @RequestBody Debt updatedDebt) {

        return ResponseEntity.ok(updatedDebt);
    }
}
