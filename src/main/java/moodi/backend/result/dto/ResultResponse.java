package moodi.backend.result.dto;

import lombok.Getter;
import lombok.Setter;
import moodi.backend.result.domain.Result;

import java.util.List;

@Getter
@Setter
public class ResultResponse {

    private Long id;
    private String story;
    private String question;
    private List<String> choices; // <-- String → List<String>
    private String answer;
    private String userInput;

    public ResultResponse(Result result) {
        this.id = result.getId();
        this.story = result.getStory();
        this.question = result.getQuestion();
        this.choices = result.getChoices(); // getChoices()는 List<String> 반환
        this.answer = result.getAnswer();
        this.userInput = result.getUserInput();
    }

}
