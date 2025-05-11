package moodi.backend.result.dto;

import lombok.Getter;
import moodi.backend.result.domain.Result;
import java.util.List;

@Getter
public class UpdateResultRequest {
    private String story;
    private String question;
    private List<String> choices;  // List<String>으로 choices를 전달받음
    private String answer;
    private String userInput;

    public UpdateResultRequest(String story, String question, List<String> choices, String answer, String userInput) {
        this.story = story;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.userInput = userInput;
    }

    public void updateEntity(Result result) {
        result.setStory(story);
        result.setQuestion(question);

        // List<String>을 Result 엔티티의 setChoices 메서드를 통해 처리
        result.setChoices(choices);  // 엔티티의 setChoices가 JSON 변환을 처리

        result.setAnswer(answer);
        result.setUserInput(userInput);
    }
}
