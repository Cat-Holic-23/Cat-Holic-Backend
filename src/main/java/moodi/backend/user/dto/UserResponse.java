package moodi.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {
    private String username;
    private String nickname;
    private String gender;
    private int age;
    private String interest;
}
