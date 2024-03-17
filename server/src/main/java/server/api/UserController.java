package server.api;

import commons.Currency;
import commons.Event;
import commons.User;
import org.springframework.web.bind.annotation.*;
import server.database.EmaiRepository;
import server.database.UserRepository;
import server.services.implementations.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private EmaiRepository emailRep;
    private UserRepository userRep;
    private UserServiceImpl userService;

    /**
     * constructor for a User
     * @param emailRep email repository to work with the db
     * @param userRep user repository to work with the db
     */
    public UserController(EmaiRepository emailRep, UserRepository userRep, UserServiceImpl userService) {
        this.emailRep = emailRep;
        this.userRep = userRep;
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
        userRep.save(user);
        return user;
    }

    @GetMapping("/events")
    public List<Event> getEvents(@RequestParam("userId") Long userId) {
        return userService.getEvents(userId);
    }


}
