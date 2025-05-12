package moodi.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LearningHistorySummaryRequest {
    private int age;

    @JsonProperty("social_situation")
    private String socialSituation;

    @JsonProperty("learning_history_summary")
    private String learningHistorySummary;
}
