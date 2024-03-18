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
@RequestMapping("/api/event")
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
    public Event getEventById(@PathVariable("id") long id){
        if (id < 0 || !db.existsById(id)) {
            return null;
        }
        return db.findById(id).get();
    }

    /**
     * Gets Event by inviteCode
     * @param inviteCode the inviteCode
     * @return the ResponseEntity
     */
    @GetMapping("/inviteCode/{inviteCode}")
    public ResponseEntity<Event> getEventByInviteCode(@PathVariable("inviteCode") String inviteCode){
        Event event = db.findByInviteCode(inviteCode);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    /**
     * Deletes the Event by id
     * @param id the id of the event
     * @return ResponseEntity that tells that it worked/didn't work
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") long id) {
        if (!db.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        db.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}

