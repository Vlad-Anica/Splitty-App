package server.services.implementations;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.services.interfaces.DebtService;

import java.util.List;
import java.util.Optional;

@Service
public class DebtServiceImpl implements DebtService {
    @Autowired
    private DebtRepository debtRep;

    public DebtServiceImpl(DebtRepository debtRep) {
        this.debtRep = debtRep;
    }
    @Override
    public List<Debt> findAll() {
        return debtRep.findAll();
    }

    @Override
    public Optional<Debt> findById(long id) {
        return debtRep.findById(id);
    }

    @Override
    public Debt save(Debt debt) {
        debtRep.save(debt);
        return debt;
    }

    @Override
    public boolean existsById(long id) {
        return debtRep.existsById(id);
    }

    @Override
    public Debt getReferenceById(long id) {
        return debtRep.getReferenceById(id);
    }
}
