package moodi.backend.user.repository;

import moodi.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    //username을 받아 DB 테이블에서 회원을 조회하는 메서드
    User findByUsername(String username);


}
