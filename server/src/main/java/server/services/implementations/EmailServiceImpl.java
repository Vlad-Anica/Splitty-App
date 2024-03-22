package server.services.implementations;

import commons.Email;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.EmailRepository;
import server.services.interfaces.EmaiService;

import java.util.List;

public class EmailServiceImpl implements EmaiService {
    @Autowired
    EmailRepository emailRep;

    public EmailServiceImpl(EmailRepository emailRep) {
        this.emailRep = emailRep;
    }

    @Override
    public Email save(Email email) {
        emailRep.save(email);
        return email;
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
    public Email findById(long id) {
        return emailRep.findById(id).get();
    }
}
