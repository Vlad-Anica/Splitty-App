package server.services.interfaces;

import commons.Expense;

import java.util.List;

public interface ExpenseService {
    public Expense save(Expense expense);
    public List<Expense> findAll();
    public boolean existsById(long id);
    public Expense findById(long id);
}
