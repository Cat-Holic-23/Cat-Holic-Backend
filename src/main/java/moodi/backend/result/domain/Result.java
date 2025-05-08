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
@Builder // 빌더 패턴 적용
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
    @Column(columnDefinition = "TEXT")
    private String choices;

    // 정답
    private String answer;

    // 사용자 입력 답안
    @Column(name = "user_input")
    private String userInput;
}
