package server.api;

import commons.Admin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
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
}
