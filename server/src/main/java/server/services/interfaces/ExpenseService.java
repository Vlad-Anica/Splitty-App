package server.services.interfaces;

import commons.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
    public Expense save(Expense expense);
    public List<Expense> findAll();
    public boolean existsById(long id);
    public Optional<Expense> findById(long id);
    public Expense getReferenceById(long id);
}
