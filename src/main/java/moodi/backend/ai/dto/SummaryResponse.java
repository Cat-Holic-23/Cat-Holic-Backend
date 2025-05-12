package moodi.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import moodi.backend.result.domain.Result;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private String story;
    private String question;

    @JsonProperty("correct_answer")
    private String answer;

    @JsonProperty("user_selected")
    private String userInput;

    public static SummaryResponse fromResult(Result result) {
        SummaryResponse dto = new SummaryResponse();
        dto.setStory(result.getStory());
        dto.setQuestion(result.getQuestion());
        dto.setAnswer(result.getAnswer());
        dto.setUserInput(result.getUserInput());
        return dto;
    }
}
