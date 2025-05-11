package moodi.backend.ai.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.ai.dto.CheckStoryResponse;
import moodi.backend.result.domain.Result;
import moodi.backend.result.repository.ResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ResultRepository resultRepository;
    private final RestTemplate restTemplate;

    public String sendToAiServer(Long resultId) {
        // 인증된 사용자 이름 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 결과 조회
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Result not found"));

        // 권한 검사
        if (!result.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(FORBIDDEN, "Access denied");
        }

        // 요청 DTO 구성
        CheckStoryResponse requestDto = new CheckStoryResponse();
        requestDto.setStory(result.getStory());
        requestDto.setQuestion(result.getQuestion());
        requestDto.setChoices(result.getChoices());
        requestDto.setAnswer(result.getAnswer());
        requestDto.setUserInput(result.getUserInput());

        // AI 서버 요청
        String aiServerUrl = "http://218.156.44.3:2323/gemini/story-check"; // 실제 URL로 수정
        ResponseEntity<String> response = restTemplate.postForEntity(aiServerUrl, requestDto, String.class);

        return response.getBody(); // 단순 문자열 반환 또는 JSON 파싱 가능
    }
}
