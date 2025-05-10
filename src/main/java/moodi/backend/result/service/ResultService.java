package moodi.backend.result.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.result.domain.Result;
import moodi.backend.result.dto.AddResultRequest;
import moodi.backend.result.dto.UpdateResultRequest;
import moodi.backend.result.repository.ResultRepository;
import moodi.backend.user.domain.User;
import moodi.backend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor // final이나 @NotNull 필드의 생성자 추가
@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final UserRepository userRepository;

    // result 추가
    public Result save(AddResultRequest request, String username) {
        User user = userRepository.findByUsername(username);

        return resultRepository.save(request.toEntity(user));
    }

    // result 조회
    public List<Result> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        return resultRepository.findByUser(user);
    }

    // result 수정
    @Transactional
    public Result update(Long id, UpdateResultRequest request, String username) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 결과가 없습니다. id=" + id));

        if (!result.getUser().getUsername().equals(username)) {
            throw new SecurityException("결과 수정 권한이 없습니다.");
        }

        request.updateEntity(result); // DTO를 통해 업데이트
        return result; // 변경감지로 자동 업데이트
    }

    // result 삭제
    public void deleteResult(Long id) {
        Optional<Result> result = resultRepository.findById(id);
        if (result.isPresent()) {
            resultRepository.delete(result.get());
        } else {
            throw new RuntimeException("Result not found with id " + id);
        }
    }
}
