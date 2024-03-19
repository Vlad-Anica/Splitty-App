package server.api;

import commons.*;
import commons.Date;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import server.database.PersonRepository;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.PersonRepository;

import java.util.*;


@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private final EventRepository eventRep;
    private PersonRepository personRep;
    private ExpenseRepository expenseRep;
    // private TagRepository tagRep

    /**
     * Constructor for an EventController
     *
     * @param eventRep   repository for Event
     * @param personRep  repository for Person
     * @param expenseRep repository for Expense
     */
    public EventController(EventRepository eventRep, PersonRepository personRep, ExpenseRepository expenseRep) {
        this.eventRep = eventRep;
        this.personRep = personRep;
        this.expenseRep = expenseRep;
    }

    /**
     * Creates and saves an Event to the database with the specified fields.
     * @param name name to use for the new Event
     * @param description description to use for the new Event
     * @param tags List of Tags to use for the new Event
     * @param date Date the Event will take place on
     * @param participantIDs List of Person IDs of Persons that will attend the Event
     * @param expenseIDs List of Expense IDs of Expenses associated with the Event
     * @return the created Event Object
     */
    @PostMapping("/")
    public Event createEvent(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("tags") List<Tag> tags,
                             @RequestParam("date") Date date,
                             @RequestParam("participantIDs") List<Long> participantIDs,
                             @RequestParam("expenseIDs") List<Long> expenseIDs) {
        if(description == null || date == null || name == null || tags == null) {
            System.out.println("Process aborted, null arguments received.");
            return null;
        }
        ArrayList<Person> participants = new ArrayList<>();
        ArrayList<Expense> expenses = new ArrayList<>();
        Event event = null;
        try {
            for(Long pID : participantIDs) {
                personRep.getReferenceById(pID);
                participants.add(personRep.getReferenceById(pID));
            }
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Participant found.");
        }
        try {
            for(Long eID : expenseIDs) {
                expenseRep.getReferenceById(eID);
                expenses.add(expenseRep.getReferenceById(eID));
            }
        }
        catch (EntityNotFoundException e) {
            System.out.println("Invalid ID, no Expense found.");
        }

        event = new Event(name, description, tags, date, participants, expenses);
        eventRep.save(event);
        return event;
    }

    @GetMapping(path = {"", "/"})
    public List<Event> getAll() {
        System.out.println("Find people...");
        return eventRep.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {

        if (id < 0 || !eventRep.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(eventRep.findById(id).get());
    }


    @GetMapping("event/{id}")
    public Event getEventById(@PathVariable("id") long id) {
        if (id < 0 || !eventRep.existsById(id)) {
            return null;
        }
        return eventRep.findById(id).get();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


}

