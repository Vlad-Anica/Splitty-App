package server.services.implementations;

import commons.Event;
import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;
import server.services.interfaces.EventService;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private EventRepository eventRep;
    private PersonRepository personRep;
    private ExpenseRepository expenseRep;
    public EventServiceImpl(EventRepository eventRep, PersonRepository personRep,
                            ExpenseRepository expenseRep) {
        this.eventRep = eventRep;
        this.expenseRep = expenseRep;
        this.personRep = personRep;
    }

    @Override
    public Event save(Event event) {
        return eventRep.save(event);
    }

    @Override
    public List<Event> findAll() {
        return eventRep.findAll();
    }

    @Override
    public Optional<Event> findById(long id) {
        return eventRep.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return eventRep.existsById(id);
    }

    @Override
    public Event findByInviteCode(String inviteCode) {
        return eventRep.findByInviteCode(inviteCode);
    }

    @Override
    public void deleteById(long id) {
        eventRep.deleteById(id);
    }

    @Override
    public ResponseEntity<List<Expense>> getExpenses(long id) {
        if (id < 0 || existsById(id))
            return ResponseEntity.badRequest().build();

        if (findById(id).isEmpty())
            return ResponseEntity.badRequest().build();

        Event event = findById(id).get();

        return ResponseEntity.ok(event.getExpenses());

    }

    public ResponseEntity<Double> getExpensesSum(long id) {

        if (id < 0 || existsById(id))
            return ResponseEntity.badRequest().build();

        if (findById(id).isEmpty())
            return ResponseEntity.badRequest().build();

        List<Expense> expenses = getExpenses(id).getBody();

        if (expenses == null)
            return ResponseEntity.ok(0.0);

        double sum = 0;
        for (Expense expense : expenses)
            sum += expense.getAmount();

        return ResponseEntity.ok(sum);
    }

}
