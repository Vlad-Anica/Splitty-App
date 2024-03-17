package server.api;

import commons.Person;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;
import commons.Debt;
import server.database.ExpenseRepository;
import server.database.PersonRepository;

@RestController
@RequestMapping("/debt")
public class DebtController {
    private DebtRepository debtRep;
    private PersonRepository personRep;
    private ExpenseRepository expenseRep;
    public DebtController(DebtRepository debtRep, PersonRepository personRep, ExpenseRepository expenseRep) {
        this.debtRep = debtRep;
        this.personRep = personRep;
        this.expenseRep = expenseRep;
    }

    @PostMapping("create")
    public Debt createDebt(@RequestParam("giver") Long giverId,
                           @RequestParam("receiver") Long receiverId, @RequestParam("expense") Long expenseId,
                           @RequestParam("amount") Double amount) {
        Debt debt = new Debt(personRep.getReferenceById(giverId), personRep.getReferenceById(receiverId),
                expenseRep.getReferenceById(expenseId), amount);
        debtRep.save(debt);
        return debt;
    }
}
