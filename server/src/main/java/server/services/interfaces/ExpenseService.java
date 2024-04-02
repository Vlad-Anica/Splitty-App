package server.services.interfaces;

import commons.Expense;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
    public Expense save(Expense expense);
    public List<Expense> findAll();
    public boolean existsById(long id);
    public Optional<Expense> findById(long id);
    public ResponseEntity<Expense> add(Expense expense);
    public Expense getReferenceById(long id);
}
