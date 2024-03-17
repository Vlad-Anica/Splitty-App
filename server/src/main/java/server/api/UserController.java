package server.api;

import commons.Currency;
import commons.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.EmaiRepository;
import server.database.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
    private EmaiRepository emailRep;
    private UserRepository userRep;

    /**
     * constructor for a User
     * @param emailRep email repository to work with the db
     * @param userRep user repository to work with the db
     */
    public UserController(EmaiRepository emailRep, UserRepository userRep) {
        this.emailRep = emailRep;
        this.userRep = userRep;
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


}
