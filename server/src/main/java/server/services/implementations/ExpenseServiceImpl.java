package server.services.implementations;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Expense getReferenceById(long id) {
        return expenseRep.getReferenceById(id);
    }
}
