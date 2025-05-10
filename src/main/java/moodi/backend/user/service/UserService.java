package moodi.backend.user.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.user.domain.User;
import moodi.backend.user.dto.UserResponse;
import moodi.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserInfo(String username) {
        // 현재 사용자 정보 조회
        User user = userRepository.findByUsername(username);

        // User 엔티티를 UserDTO로 변환하여 반환
        return UserResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .age(user.getAge())
                .interest(user.getInterest())
                .build();
    }
}
