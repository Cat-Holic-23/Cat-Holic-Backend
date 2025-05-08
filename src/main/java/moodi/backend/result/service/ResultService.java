package moodi.backend.result.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.result.domain.Result;
import moodi.backend.result.dto.AddResultRequest;
import moodi.backend.result.repository.ResultRepository;
import moodi.backend.user.domain.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // final이나 @NotNull 필드의 생성자 추가
@Service
public class ResultService {

    private final ResultRepository resultRepository;

    // result 추가
    public Result save(AddResultRequest request, User user) {
        return resultRepository.save(request.toEntity(user));
    }
}
