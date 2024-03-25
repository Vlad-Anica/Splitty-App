package server.api;

import commons.Currency;
import commons.Event;
import commons.User;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.EmailService;
import server.services.interfaces.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private EmailService emailService;
    private UserService userService;

    /**
     * constructor for a User
     * @param emailService email repository to work with the db
     * @param userService user repository to work with the db
     */
    public UserController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
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

    @GetMapping("/events")
    public List<Event> getEvents(@RequestParam("userId") Long userId) {
        return userService.getEvents(userId);
    }




}
