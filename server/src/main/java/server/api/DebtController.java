package server.api;

import commons.Debt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.DebtRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;

@RestController
@RequestMapping("/debt")
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
        Debt debt = new Debt(personRep.getReferenceById(giverId), personRep.getReferenceById(receiverId),
                expenseRep.getReferenceById(expenseId), amount);
        debtRep.save(debt);
        return debt;
    }
}
