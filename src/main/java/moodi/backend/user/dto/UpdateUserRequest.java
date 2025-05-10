package moodi.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String gender;
    private Integer age;
    private String interest;
    private String nickname;
}
