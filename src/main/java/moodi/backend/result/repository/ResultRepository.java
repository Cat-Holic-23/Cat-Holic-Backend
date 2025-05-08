package moodi.backend.result.repository;

import moodi.backend.result.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {

}
