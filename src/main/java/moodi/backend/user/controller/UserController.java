package moodi.backend.user.controller;

import lombok.RequiredArgsConstructor;
import moodi.backend.user.CustomUserDetails;
import moodi.backend.user.domain.User;
import moodi.backend.user.dto.UpdateUserRequest;
import moodi.backend.user.dto.UserResponse;
import moodi.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/users")
    public ResponseEntity<User> updateUserInfo(@RequestBody UpdateUserRequest updateUserRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // 로그인한 사용자 정보 (JWT에서 사용자 정보 추출)
        String username = customUserDetails.getUsername();;  // 일반적으로 username (혹은 사용자 id)

        // 유저 정보 수정
        User updatedUser = userService.updateUserInfo(username, updateUserRequest);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // 권한이 없으면 수정 불가
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();

        userService.deleteUser(username);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // POST 요청 처리
    @PostMapping("/point")
    public String addPoints(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        try {
            // 사용자에게 30점을 추가
            userService.addPointsToUser(username, 30);
            return "Points added successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
