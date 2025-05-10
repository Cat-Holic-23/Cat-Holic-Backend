package moodi.backend.user.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.user.domain.User;
import moodi.backend.user.dto.UpdateUserRequest;
import moodi.backend.user.dto.UserResponse;
import moodi.backend.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    // 유저 정보 수정 서비스
    public User updateUserInfo(String username, UpdateUserRequest updateUserRequest) {
        // 현재 로그인된 사용자 정보로 유저 찾기
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return null;  // 유저가 존재하지 않으면 null 반환
        }

        // 유저 정보 수정 (username과 password는 제외)
        if (updateUserRequest.getGender() != null) {
            user.setGender(updateUserRequest.getGender());
        }
        if (updateUserRequest.getAge() != null) {
            user.setAge(updateUserRequest.getAge());
        }
        if (updateUserRequest.getInterest() != null) {
            user.setInterest(updateUserRequest.getInterest());
        }
        if (updateUserRequest.getNickname() != null) {
            user.setNickname(updateUserRequest.getNickname());
        }

        // 수정된 유저 저장
        return userRepository.save(user);
    }
}
