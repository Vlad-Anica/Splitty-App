package server.api;

import commons.Admin;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    public AdminController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * check the password
     * @param password password attempt
     * @return is correct
     */
    @PostMapping("/checkPassword")
    public boolean checkPassword(@RequestBody String password) {
        // Assuming Admin.isCorrectPassword(String password) is a static method
        return Admin.isCorrectGeneratedPassword(password);
    }

    @GetMapping("/eventsOrderedByLastModificationDate")
    public List<Event> getEventsOrderedByLastModificationDate() {
        return eventService.getEventsOrderedByLastModifiedDate().get();
    }

    @GetMapping("/eventsOrderedByCreationDate")
    public List<Event> getEventsOrderedByCreationDate() {
        return eventService.getEventsOrderedByCreationDate().get();
    }

    @GetMapping("/eventsOrderedByName")
    public List<Event> getEventsOrderedByName() {
        return eventService.getEventsOrderedByName().get();
    }
}
