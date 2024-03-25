package server.services.implementations;

import commons.*;

import static commons.Currency.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.EventRepository;
import server.database.UserRepository;
import server.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;
    @MockBean
    private UserRepository repo;
    @MockBean
    private EventRepository eventRepo;
    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        repo = mock(UserRepository.class);
        userService = new UserServiceImpl(repo, eventRepo);

        user1 = User.builder().id(1L).firstName("John").lastName("Smith").IBAN("").BIC("11111111")
                .email("john.smith@gmail.com").preferredCurrency(USD).build();

        user2 = User.builder().id(2L).firstName("Jane").lastName("Wesson").IBAN("").BIC("11111111")
                .email("jane.wesson@gmail.com").preferredCurrency(EUR).build();

        users = new ArrayList<>();
    }

    @Test
    void findAllTestFull(){
        users.add(user1);
        users.add(user2);

        when(repo.findAll()).thenReturn(users);
        List<User> result = userService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void findAllTestEmpty(){
        when(repo.findAll()).thenReturn(users);
        List<User> result = userService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void saveTest(){
        when(repo.save(user1)).thenReturn(user1);
        User result = userService.save(user1);
        assertEquals(user1, result);
    }

    @Test
    void saveTestNull(){
        User p = null;
        when(repo.save(p)).thenReturn(null);
        User result = userService.save(null);
        assertNull(result);
    }

    @Test
    void findByIdTest(){
        users.add(user1);
        users.add(user2);
        Optional<User> optUser1 = Optional.of(user1);
        Optional<User> optUser2 = Optional.of(user2);
        Optional<User> optUser3 = Optional.empty();

        when(repo.findById(1L)).thenReturn(optUser1);
        when(repo.findById(2L)).thenReturn(optUser2);
        when(repo.findById(3L)).thenReturn(optUser3);

        Optional<User> result1 = userService.findById(1L);
        Optional<User> result2 = userService.findById(2L);
        Optional<User> result3 = userService.findById(3L);

        assertEquals(optUser1, result1);
        assertEquals(optUser2, result2);
        assertNotEquals(optUser1, result2);
        assertNotEquals(optUser2, result1);
        assertEquals(Optional.empty(), result3);
    }

    @Test
    void existByIdTest(){
        users.add(user1);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        boolean doesExist = userService.existsById(1L);
        boolean doesNotExist = userService.existsById(2L);
        assertTrue(doesExist);
        assertFalse(doesNotExist);
    }

    @Test
    void existByIdTestNegative(){
        when(repo.existsById(-1L)).thenReturn(false);
        boolean doesNotExist = userService.existsById(-1L);
        assertFalse(doesNotExist);
    }

    @Test
    void getEventsTest(){

        List<Event> result1 = userService.getEvents(1L).getBody();
        List<Event> result2 = userService.getEvents(2L).getBody();

        //This test doesn't make much sense because I believe the class it is testing also doesn't make much sense

        assertEquals(result2, result1);

    }

}
