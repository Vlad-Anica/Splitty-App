package server.database;

import commons.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmaiRepository extends JpaRepository<Email, Long> {
}
