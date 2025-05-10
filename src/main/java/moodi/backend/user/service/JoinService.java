package moodi.backend.user.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.user.domain.User;
import moodi.backend.user.dto.JoinDTO;
import moodi.backend.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {
        // 중복 확인
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String nickname = joinDTO.getNickname();
        String gender = joinDTO.getGender();
        int age = joinDTO.getAge();
        String interest = joinDTO.getInterest();

        User data = new User();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_USER");
        data.setNickname(nickname);
        data.setGender(gender);
        data.setAge(age);
        data.setInterest(interest);
        
        userRepository.save(data);
    }
}
