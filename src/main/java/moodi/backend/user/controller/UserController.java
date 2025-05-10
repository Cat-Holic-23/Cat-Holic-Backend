package moodi.backend.user.controller;

import lombok.RequiredArgsConstructor;
import moodi.backend.user.CustomUserDetails;
import moodi.backend.user.dto.UserResponse;
import moodi.backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();  // 인증된 사용자 정보
        UserResponse userResponse = userService.getUserInfo(username);

        return ResponseEntity.ok(userResponse);
    }

}
