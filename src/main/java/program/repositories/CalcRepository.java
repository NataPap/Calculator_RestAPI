package program.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import program.entities.Expression;

import java.util.List;

@Repository

public interface CalcRepository extends JpaRepository<Expression, Integer> {
    List<Expression> findByResult (@Param("result") Double res);
}
