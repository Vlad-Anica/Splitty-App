package server.services.implementations;

import commons.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.database.EmailRepository;
import server.services.interfaces.EmailService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmailServiceTest {
    private EmailService emailService;
    @Mock
    private EmailRepository repo;
    private Email email1;
    private Email email2;
    private List<Email> emails;

    @BeforeEach
    public void setup(){
        emailService = new EmailServiceImpl(repo);

        email1 = Email.builder().id(1L).address("one@gmail.com").build();

        email2 = Email.builder().id(2L).address("two@gmail.com").build();

        emails = new ArrayList<>();
    }

    @Test
    void saveTest(){
        when(repo.save(email1)).thenReturn(email1);
        Email result = emailService.save(email1);
        assertEquals(email1, result);
    }

    @Test
    void findAllTestFull(){
        emails.add(email1);
        emails.add(email2);

        when(repo.findAll()).thenReturn(emails);
        List<Email> result = emailService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void findAllTestEmpty(){
        when(repo.findAll()).thenReturn(emails);
        List<Email> result = emailService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void existByIdTest(){
        emails.add(email1);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        boolean doesExist = emailService.existsById(1L);
        boolean doesNotExist = emailService.existsById(2L);
        assertTrue(doesExist);
        assertFalse(doesNotExist);
    }

    @Test
    void existByIdTestNegative(){
        when(repo.existsById(-1L)).thenReturn(false);
        boolean doesNotExist = emailService.existsById(-1L);
        assertFalse(doesNotExist);
    }

    @Test
    public void testFindByID() {
        when(repo.findById(1L)).thenReturn(Optional.of(email1));
        assertEquals(email1, emailService.findById(1L).get());
    }
}