package server.api;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import server.database.PersonRepository;
import server.database.ExpenseRepository;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private final ExpenseRepository db;
    public ExpenseController(ExpenseRepository db) {

        this.db = db;
    }

    @GetMapping(path = { "", "/" })
    public List<Expense> getAll() {
        System.out.println("Find people...");
        return db.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") long id) {

        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(db.findById(id).get());
    }


    @GetMapping("participants/{id}")
    public List<Debt> getParticipantsById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get().getDebtList();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

