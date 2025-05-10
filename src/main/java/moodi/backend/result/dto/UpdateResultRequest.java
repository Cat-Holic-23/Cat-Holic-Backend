package moodi.backend.result.dto;

import lombok.Getter;
import moodi.backend.result.domain.Result;

@Getter
public class UpdateResultRequest {
    private String story;
    private String question;
    private String choices;
    private String answer;
    private String userInput;

    public UpdateResultRequest(String story, String question, String choices, String answer, String userInput) {
        this.story = story;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.userInput = userInput;
    }

    public void updateEntity(Result result) {
        result.setStory(story);
        result.setQuestion(question);
        result.setChoices(choices);
        result.setAnswer(answer);
        result.setUserInput(userInput);
    }
}
