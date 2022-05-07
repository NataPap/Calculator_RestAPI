package program.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import program.entities.Expression;

@Repository

public interface CalcRepository extends JpaRepository<Expression, Integer> {
}
