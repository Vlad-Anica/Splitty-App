package server.services.interfaces;

import commons.Email;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface EmailService {
    public Email save(Email email);
    public List<Email> findAll();
    public boolean existsById(long id);
    public Optional<Email> findById(long id);
}
