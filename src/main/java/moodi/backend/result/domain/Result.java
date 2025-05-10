package moodi.backend.result.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moodi.backend.user.domain.User;

@Entity
@Table(name = "result")
@Getter
@Setter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 사회상황이야기
    private String story;

    // 질문
    private String question;

    // 선택지
    private String choices;

    // 정답
    private String answer;

    // 사용자 입력 답안
    @Column(name = "user_input")
    private String userInput;

    public Result() {}

    @Builder
    public Result(Long id, User user, String story, String question, String choices, String answer, String userInput) {
        this.id = id;
        this.user = user;
        this.story = story;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.userInput = userInput;
    }
}
