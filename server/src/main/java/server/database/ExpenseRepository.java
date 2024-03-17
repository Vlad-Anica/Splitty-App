package server.database;

import ch.qos.logback.core.encoder.EchoEncoder;
import commons.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
