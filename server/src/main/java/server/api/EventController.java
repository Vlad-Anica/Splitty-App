package server.api;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import server.database.PersonRepository;
import server.database.EventRepository;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private final EventRepository db;
    public EventController(EventRepository db) {

        this.db = db;
    }

    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        System.out.println("Find people...");
        return db.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {

        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(db.findById(id).get());
    }


    @GetMapping("event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return null;
        }

        return ResponseEntity.ok(db.findById(id).get());
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
