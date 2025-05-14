package moodi.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    private String username;
    private String password;
    private String nickname;
    private String gender;
    private int age;
    private String interest;
    private int point;
}
