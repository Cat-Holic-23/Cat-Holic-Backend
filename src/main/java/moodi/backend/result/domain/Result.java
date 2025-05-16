package moodi.backend.result.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moodi.backend.user.domain.User;

import java.util.List;

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

    @Lob
    private String story;

    private String question;

    // 실제 DB에는 JSON형식의 데이터를 문자열로 저장
    @Column(name = "choices", columnDefinition = "TEXT")
    private String choicesJson;

    // 비즈니스 로직에서는 List<String> 형태로 사용
    @Transient
    private List<String> choices;

    private String answer;

    @Column(name = "user_input")
    private String userInput;

    public Result() {}

    @Builder
    public Result(Long id, User user, String story, String question, List<String> choices, String answer, String userInput) {
        this.id = id;
        this.user = user;
        this.story = story;
        this.question = question;
        setChoices(choices); // JSON 변환 자동 수행
        this.answer = answer;
        this.userInput = userInput;
    }

    public void setChoices(List<String> choices) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.choicesJson = objectMapper.writeValueAsString(choices);
            this.choices = choices;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("선택지를 JSON으로 변환하는 데 실패했습니다.", e);
        }
    }

    public List<String> getChoices() {
        if (choices == null && choicesJson != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                this.choices = objectMapper.readValue(choicesJson, new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 문자열을 선택지 리스트로 변환하는 데 실패했습니다.", e);
            }
        }
        return choices;
    }

    public String getChoicesJson() {
        return choicesJson;
    }

    public void setChoicesJson(String choicesJson) {
        this.choicesJson = choicesJson;
    }
}
