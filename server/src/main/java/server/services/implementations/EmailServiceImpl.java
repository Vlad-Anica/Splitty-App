package server.services.implementations;

import commons.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EmailRepository;
import server.services.interfaces.EmailService;

import java.util.List;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    EmailRepository emailRep;

    public EmailServiceImpl(EmailRepository emailRep) {
        this.emailRep = emailRep;
    }

    @Override
    public Email save(Email email) {
        return emailRep.save(email);
    }

    @Override
    public List<Email> findAll() {
        return emailRep.findAll();
    }

    @Override
    public boolean existsById(long id) {
        return emailRep.existsById(id);
    }

    @Override
    public Optional<Email> findById(long id) {
        return emailRep.findById(id);
    }
}
