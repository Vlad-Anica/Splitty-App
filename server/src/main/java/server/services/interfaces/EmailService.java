package server.services.interfaces;

import commons.Email;

import java.util.List;

public interface EmailService {
    public Email save(Email email);
    public List<Email> findAll();
    public boolean existsById(long id);
    public Email findById(long id);
}
