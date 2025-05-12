package moodi.backend.ai.service;

import lombok.RequiredArgsConstructor;
import moodi.backend.ai.dto.*;
import moodi.backend.result.domain.Result;
import moodi.backend.result.repository.ResultRepository;
import moodi.backend.user.domain.User;
import moodi.backend.user.repository.UserRepository;
import org.springframework.http.*;
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

    public String sendRecentResultsToAi(String username, SocialSituationRequest socialSituationInput) {
        List<Result> results = resultRepository.findTop10ByUserUsernameOrderByIdDesc(username);
        List<SummaryResponse> items = results.stream()
                .map(r -> new SummaryResponse(
                        r.getStory(),
                        r.getQuestion(),
                        r.getUserInput(),    // user_selected
                        r.getAnswer()        // correct_answer
                ))
                .collect(Collectors.toList());

        SummaryRequest request = new SummaryRequest(items);

        String aiUrl = "http://218.156.44.3:2323/local/summation";

        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SummaryRequest> entity1 = new HttpEntity<>(request, headers1);

        ResponseEntity<String> response = restTemplate.postForEntity(aiUrl, entity1, String.class);

        // 두번째 API 요청
        String historySummary = response.getBody();

        // DB에서 정보 조회
        User user = userRepository.findByUsername(username);

        // 5. 최종 요청 DTO 구성
        LearningHistorySummaryRequest finalRequest = new LearningHistorySummaryRequest();
        finalRequest.setAge(user.getAge());
        finalRequest.setSocialSituation(String.valueOf(socialSituationInput));
        finalRequest.setLearningHistorySummary(historySummary);

        // 6. 두 번째 AI 요청 보내기
        try {
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LearningHistorySummaryRequest> entity2 = new HttpEntity<>(finalRequest, headers2);

            String finalAiUrl = "http://218.156.44.3:2323/gemini/story_gen";  // 두 번째 요청용 URL
            ResponseEntity<String> finalResponse = restTemplate.postForEntity(finalAiUrl, entity2, String.class);

            return finalResponse.getBody();
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 상황 요청 실패", e);
        }
    }
}
