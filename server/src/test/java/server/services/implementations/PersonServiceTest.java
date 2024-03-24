package server.services.implementations;

import commons.*;

import static commons.Currency.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.PersonRepository;
import server.services.interfaces.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    private PersonService personService;
    @MockBean
    private PersonRepository repo;
    private Person person1;
    private Person person2;
    private List<Person> people;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        repo = mock(PersonRepository.class);
        personService = new PersonServiceImpl(repo);
        //Creating the Tester Event
        Event testerEvent = new Event();
        //Creating the Tester User
        User testerUser = new User();
        //Creating Tester debtList
        ArrayList<Debt> testerDebtList = new ArrayList<>();

        person1 = Person.builder().id(1L).BIC("11111111").IBAN("").email("john.smith@gmail.com")
                .firstName("John").lastName("Smith").preferredCurrency(USD).totalDebt(20L)
                .event(testerEvent).user(testerUser).debtList(testerDebtList).build();

        person2 = Person.builder().id(2L).BIC("22222222").IBAN("").email("jane.wesson@gmail.com")
                .firstName("Jane").lastName("Wesson").preferredCurrency(EUR).totalDebt(10L)
                .event(testerEvent).user(testerUser).debtList(testerDebtList).build();
        people = new ArrayList<>();
    }

    @Test
    void findAllTestFull(){
        people.add(person1);
        people.add(person2);

        when(repo.findAll()).thenReturn(people);
        List<Person> result = personService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void findAllTestEmpty(){
        when(repo.findAll()).thenReturn(people);
        List<Person> result = personService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void saveTest(){
        when(repo.save(person1)).thenReturn(person1);
        Person result = personService.save(person1);
        assertEquals(person1, result);
    }

    @Test
    void saveTestNull(){
        Person p = null;
        when(repo.save(p)).thenReturn(null);
        Person result = personService.save(null);
        assertNull(result);
    }

    @Test
    void findByIdTest(){
        people.add(person1);
        people.add(person2);
        Optional<Person> optPerson1 = Optional.of(person1);
        Optional<Person> optPerson2 = Optional.of(person2);
        Optional<Person> optPerson3 = Optional.empty();

        when(repo.findById(1L)).thenReturn(optPerson1);
        when(repo.findById(2L)).thenReturn(optPerson2);
        when(repo.findById(3L)).thenReturn(optPerson3);

        Optional<Person> result1 = personService.findById(1L);
        Optional<Person> result2 = personService.findById(2L);
        Optional<Person> result3 = personService.findById(3L);

        assertEquals(optPerson1, result1);
        assertEquals(optPerson2, result2);
        assertNotEquals(optPerson1, result2);
        assertNotEquals(optPerson2, result1);
        assertEquals(Optional.empty(), result3);
    }

    @Test
    void existByIdTest(){
        people.add(person1);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        boolean doesExist = personService.existsById(1L);
        boolean doesNotExist = personService.existsById(2L);
        assertTrue(doesExist);
        assertFalse(doesNotExist);
    }

    @Test
    void existByIdTestNegative(){
        when(repo.existsById(-1L)).thenReturn(false);
        boolean doesNotExist = personService.existsById(-1L);
        assertFalse(doesNotExist);
    }

    @Test
    void findReferenceByIdTest(){
        people.add(person1);
        people.add(person2);

        when(repo.getReferenceById(1L)).thenReturn(person1);
        when(repo.getReferenceById(2L)).thenReturn(person2);
        when(repo.getReferenceById(3L)).thenReturn(null);

        Person result1 = personService.getReferenceById(1L);
        Person result2 = personService.getReferenceById(2L);
        Person result3 = personService.getReferenceById(3L);

        assertEquals(person1, result1);
        assertEquals(person2, result2);
        assertNotEquals(person1, result2);
        assertNotEquals(person2, result1);
        assertNull(result3);
    }

}
