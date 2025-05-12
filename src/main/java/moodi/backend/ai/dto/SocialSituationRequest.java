package moodi.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialSituationRequest {
    private int age;
    private String socialSituation;
}
