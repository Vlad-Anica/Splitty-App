package server.services.interfaces;

import commons.Debt;

import java.util.List;
import java.util.Optional;

public interface DebtService {
    public List<Debt> findAll();
    public Optional<Debt> findById(long id);
    public Debt save(Debt debt);
    public boolean existsById(long id);
    public Debt getReferenceById(long id);

    List<Debt> getByPersonId(long id);
}
