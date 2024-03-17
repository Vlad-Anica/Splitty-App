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

    public UserController(EmaiRepository emailRep, UserRepository userRep) {
        this.emailRep = emailRep;
        this.userRep = userRep;
    }

    @PostMapping("/create")
    public User createUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                           @RequestParam("email") String email, @RequestParam("currency") Currency preferredCurency) {
        User user = new User(firstName, lastName, email, preferredCurency);
        userRep.save(user);
        return user;
    }


}
