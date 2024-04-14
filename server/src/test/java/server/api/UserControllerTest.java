package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.interfaces.EmailService;
import server.services.interfaces.EventService;
import server.services.interfaces.PersonService;
import server.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static commons.Currency.EUR;
import static commons.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private EventService eventService;
    @Mock
    private PersonService personService;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userController = new UserController(emailService , userService, eventService, personService);
        user1 = User.builder().id(1L).firstName("John").lastName("Smith").IBAN("").BIC("11111111")
                .email("john.smith@gmail.com").preferredCurrency(USD).build();

        user2 = User.builder().id(2L).firstName("Jane").lastName("Wesson").IBAN("").BIC("11111111")
                .email("jane.wesson@gmail.com").preferredCurrency(EUR).build();

    }

    @Test
    public void testAdd() {
        when(userService.save(user1)).thenReturn(user1);
        when(userService.save(user2)).thenReturn(user2);

        ResponseEntity<User> response = userController.add(user1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Assuming add() returns 201 CREATED on success
        assertEquals(user1, response.getBody());

        response = userController.add(user2);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Same assumption as above
        assertEquals(user2, response.getBody());

        when(userService.findAll()).thenReturn(List.of(user1, user2));
        List<User> users = userController.getAll();
        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }


    @Test
    public void testGetById() {

        when(userService.findById(1L)).thenReturn(Optional.of(user1));

        ResponseEntity<User> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user1.getId(), response.getBody().getId());
        assertEquals(user1.getFirstName(), response.getBody().getFirstName());
        assertEquals(user1.getLastName(), response.getBody().getLastName());
        assertEquals(user1.getIBAN(), response.getBody().getIBAN());
        assertEquals(user1.getBIC(), response.getBody().getBIC());
        assertEquals(user1.getEmail(), response.getBody().getEmail());
        assertEquals(user1.getPreferredCurrency(), response.getBody().getPreferredCurrency());
    }

    @Test
    public void testGetAll() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.findAll()).thenReturn(users);

        List<User> result = userController.getAll();
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

}