package moodi.backend.result.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moodi.backend.result.domain.Result;
import moodi.backend.user.domain.User;

@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자
@Getter
public class AddResultRequest {
    private String story;
    private String question;
    private String choices;
    private String answer;
    private String userInput;

    public Result toEntity(User user) {
        return Result.builder()
                .user(user)
                .story(story)
                .question(question)
                .choices(choices)
                .answer(answer)
                .userInput(userInput)
                .build();
    }
}
