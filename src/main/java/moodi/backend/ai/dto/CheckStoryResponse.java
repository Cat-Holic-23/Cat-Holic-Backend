package moodi.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckStoryResponse {

    private String story;
    private String question;
    private List<String> choices;

    @JsonProperty("correct_answer")
    private String answer;

    @JsonProperty("user_selected")
    private String userInput;
}
