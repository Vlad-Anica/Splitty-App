package server.services.implementations;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;
import server.services.interfaces.ExpenseService;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    private ExpenseRepository expenseRep;

    public ExpenseServiceImpl(ExpenseRepository expenseRep) {
        this.expenseRep = expenseRep;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRep.save(expense);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRep.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return expenseRep.existsById(id);
    }

    @Override
    public Optional<Expense> findById(long id) {
        return expenseRep.findById(id);
    }

    @Override
    public ResponseEntity<Expense> add(Expense expense) {
        if (expense == null || isNullOrEmpty(expense.getDescription()) ||
                expense.getReceiver() == null || expense.getDate() == null ||
                expense.getCurrency() == null)
            return ResponseEntity.badRequest().build();

        Expense saved = save(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @Override
    public Expense getReferenceById(long id) {
        return expenseRep.getReferenceById(id);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
