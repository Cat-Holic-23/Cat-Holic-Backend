package moodi.backend.ai.controller;

import lombok.RequiredArgsConstructor;
import moodi.backend.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/gemini/{resultId}")
    public ResponseEntity<?> sendToAi(@PathVariable Long resultId) {
        String aiResponse = aiService.sendToAiServer(resultId);
        return ResponseEntity.ok(aiResponse);
    }
}
