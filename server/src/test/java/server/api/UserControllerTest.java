package server.api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.interfaces.EmailService;
import server.services.interfaces.EventService;
import server.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;

import static commons.Currency.EUR;
import static commons.Currency.USD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @MockBean
    private UserService userService;
    private EmailService emailService;
    private EventService eventService;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = mock(EmailService.class);
        userService = mock(UserService.class);
        userController = new UserController(emailService , userService, eventService);
        user1 = User.builder().id(1L).firstName("John").lastName("Smith").IBAN("").BIC("11111111")
                .email("john.smith@gmail.com").preferredCurrency(USD).build();

        user2 = User.builder().id(2L).firstName("Jane").lastName("Wesson").IBAN("").BIC("11111111")
                .email("jane.wesson@gmail.com").preferredCurrency(EUR).build();

    }

    @Test
    public void testAdd() {
        when(userService.save(user1)).thenReturn(ResponseEntity.ok(user1));
        when(userService.save(user2)).thenReturn(ResponseEntity.ok(user2));
        ResponseEntity<User> response = userController.add(user1);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.add(user2);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(userService.findById(1L)).thenReturn(ResponseEntity.ok(user1));
        when(userService.findById(2L)).thenReturn(ResponseEntity.ok(user2));
        when(userService.findAll()).thenReturn(List.of(user1, user2));
        List<User> users = userController.getAll();
        assertEquals(2, users.size());
        assertEquals(user1, userController.getById(1L).getBody());
        assertEquals(user2, userController.getById(2L).getBody());
    }
    //@Test
    //blic void testGetEvent(){
    //}

    //@Test
    //public void testGetExpense(){
    //}

    @Test
    public void testGetById() {

        when(userService.findById(1L)).thenReturn(
                ResponseEntity.ok(user1));
        userController.add(user1);
        ResponseEntity<User> response = userController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Smith", response.getBody().getLastName());
        assertEquals("", response.getBody().getIBAN());
        assertEquals("11111111", response.getBody().getBIC());
        assertEquals("john.smith@gmail.com", response.getBody().getEmail());
        assertEquals(USD, response.getBody().getPreferredCurrency());
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