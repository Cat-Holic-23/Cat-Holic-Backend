package moodi.backend.result.repository;

import moodi.backend.result.domain.Result;
import moodi.backend.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUser(User user);

    // id 내림차순 기준으로 10개 데이터 가져오기
    List<Result> findTop10ByUserUsernameOrderByIdDesc(String username);
}
