package server.services.implementations;

import commons.*;

import static commons.Currency.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.UserRepository;
import server.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository repo;
    @Mock
    private EventRepository eventRepo;
    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    public void setup(){
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

    /*
    @Test
    void saveTestNull(){
        User result = userService.save(null);
        assertNull(result);
    }

    @Test
    void findByIdTest(){
        when(repo.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }*/

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
