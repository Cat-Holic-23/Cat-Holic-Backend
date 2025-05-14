package moodi.backend.ai.controller;

import lombok.RequiredArgsConstructor;
import moodi.backend.ai.dto.SocialSituationRequest;
import moodi.backend.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/gemini/{resultId}")
    public ResponseEntity<?> sendToAi(@PathVariable Long resultId) {
        String aiResponse = aiService.sendToAiServer(resultId);
        return ResponseEntity.ok(aiResponse);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> sendRecentResultsToAi(@RequestBody SocialSituationRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String response = aiService.sendRecentResultsToAi(username, request); // AI 응답을 그대로 받음

        return ResponseEntity.ok(response); // AI의 응답을 그대로 클라이언트에 반환
    }
}
