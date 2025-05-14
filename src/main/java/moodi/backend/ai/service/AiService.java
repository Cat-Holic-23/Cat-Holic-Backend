package moodi.backend.ai.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.ai.dto.*;
import moodi.backend.exception.RetryableServerException;
import moodi.backend.result.domain.Result;
import moodi.backend.result.repository.ResultRepository;
import moodi.backend.user.domain.User;
import moodi.backend.user.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AiService {

    private final UserRepository userRepository;
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

    @Retryable(
            value = { RetryableServerException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public String sendRecentResultsToAi(String username, SocialSituationRequest socialSituationInput) {
        List<Result> results = resultRepository.findTop10ByUserUsernameOrderByIdDesc(username);

        // 결과가 비어있으면 바로 story_gen에 빈 문자열을 보내도록 설정
        if (results.isEmpty()) {
            User user = userRepository.findByUsername(username);

            LearningHistorySummaryRequest finalRequest = new LearningHistorySummaryRequest();
            finalRequest.setAge(user.getAge());
            finalRequest.setSocialSituation(String.valueOf(socialSituationInput));
            finalRequest.setLearningHistorySummary(""); // 빈 문자열을 보내기

            String finalAiUrl = "http://218.156.44.3:2323/gemini/story_gen";
            ResponseEntity<String> finalResponse = postWithRetry(finalAiUrl, finalRequest);

            return finalResponse.getBody();
        }

        // 결과가 비어있지 않으면 정상적인 처리를 진행
        List<SummaryResponse> items = results.stream()
                .map(r -> new SummaryResponse(
                        r.getStory(),
                        r.getQuestion(),
                        r.getUserInput(),
                        r.getAnswer()
                ))
                .collect(Collectors.toList());

        SummaryRequest request = new SummaryRequest(items);
        String aiUrl = "http://218.156.44.3:2323/local/summation";

        // AI 서버에 요청 보내기
        ResponseEntity<String> response = postWithRetry(aiUrl, request);
        String historySummary = response.getBody();

        User user = userRepository.findByUsername(username);

        LearningHistorySummaryRequest finalRequest = new LearningHistorySummaryRequest();
        finalRequest.setAge(user.getAge());
        finalRequest.setSocialSituation(String.valueOf(socialSituationInput));
        finalRequest.setLearningHistorySummary(historySummary);

        String finalAiUrl = "http://218.156.44.3:2323/gemini/story_gen";
        ResponseEntity<String> finalResponse = postWithRetry(finalAiUrl, finalRequest);

        return finalResponse.getBody();
    }


    @Retryable(
            value = { RetryableServerException.class, RuntimeException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    private ResponseEntity<String> postWithRetry(String url, Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is5xxServerError()) {
                throw new RetryableServerException("AI 서버에서 5XX 에러 발생");
            }

            return response;
        } catch (Exception e) {
            throw new RetryableServerException("AI 서버 통신 실패", e);
        }

    }
}
