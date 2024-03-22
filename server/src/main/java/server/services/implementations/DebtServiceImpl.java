package server.services.implementations;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.DebtRepository;
import server.services.interfaces.DebtService;

import java.util.List;

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
    public Debt findById(long id) {
        return debtRep.findById(id).get();
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
}
