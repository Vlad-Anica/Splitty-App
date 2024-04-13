package server.database;

import commons.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findDebtsByReceiverId(Long personId);
    List<Debt> findDebtsByGiverId(Long personId);
    List<Debt> findDebtsByGiverIdOrReceiverId(Long giverId, Long receiverId);
}


