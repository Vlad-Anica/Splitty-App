package server.api;

import commons.Debt;
import commons.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.DebtRepository;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtRepository db;
    public DebtController(DebtRepository db) {
        this.db = db;
    }

    @GetMapping(path = {"", "/"})
    public List<Debt> getAll() {
        return db.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(db.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Debt> add(@RequestBody Debt debt) {
        System.out.println("Received Debt object: " + debt);

        if (debt== null || debt.getReceiver() == null ||
                debt.getGiver() == null ||
                debt.getAmount() < 0) {
            return ResponseEntity.badRequest().build();
        }
        debt.getGiver().getDebtList().add(debt);
        debt.getGiver().setTotalDebt(debt.getGiver().getTotalDebt() + debt.getAmount());
        Debt saved = db.save(debt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Debt> update(@PathVariable("id") long id,
                                        @RequestBody Debt updatedDebt) {

        return ResponseEntity.ok(updatedDebt);
    }

}
