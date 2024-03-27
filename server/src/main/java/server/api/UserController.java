package server.api;

import commons.Currency;
import commons.Event;
import commons.Expense;
import commons.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.EmailService;
import server.services.interfaces.EventService;
import server.services.interfaces.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private EmailService emailService;
    private UserService userService;
    private EventService eventService;
    /**
     * constructor for a User
     * @param emailService email repository to work with the db
     * @param userService user repository to work with the db
     */
    public UserController(EmailService emailService, UserService userService, EventService eventService) {
        this.emailService = emailService;
        this.userService = userService;
        this.eventService = eventService;
    }

    /**
     * creates a user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param email email of the user
     * @param preferredCurency preferred currency of the user
     * @return the user
     */
    @PostMapping("/")
    public User createUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                           @RequestParam("email") String email, @RequestParam("currency") Currency preferredCurency) {
        User user = new User(firstName, lastName, email, preferredCurency);
        return userService.save(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long userId) {
        if (!userService.existsById(userId)) {
            return (ResponseEntity<User>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userService.findById(userId).get());
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<Event>> getEvents(@PathVariable("userId") Long userId) {
        return userService.getEvents(userId);
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<List<Expense>> getExpenses(@PathVariable("id") long id) {
        return userService.getExpenses(id);
    }

    @GetMapping("/{id}/totalExpenses")
    public ResponseEntity<Double> getTotalExpenses(@PathVariable("id") long id) {

        if (id < 0 || !userService.existsById(id))
            return ResponseEntity.badRequest().build();

        if (userService.findById(id).isEmpty())
            return ResponseEntity.badRequest().build();

        User user = userService.findById(id).get();

        List<Event> events = getEvents(id).getBody();
        if (events == null)
            return ResponseEntity.notFound().build();

        double sum = 0;
        for (Event event : events) {
            if (!eventService.existsById(id) || eventService.findById(id).isEmpty())
                continue;

            sum += eventService.getExpensesSum(id).getBody();
        }

        return ResponseEntity.ok(sum);
    }
}
