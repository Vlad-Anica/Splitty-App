package server.services.interfaces;

import commons.Debt;

import java.util.List;

public interface DebtService {
    public List<Debt> findAll();
    public Debt findById(long id);
    public Debt save(Debt debt);
    public boolean existsById(long id);
}
