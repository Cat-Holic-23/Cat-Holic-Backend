package moodi.backend.result.controller;

import lombok.RequiredArgsConstructor;
import moodi.backend.result.domain.Result;
import moodi.backend.result.dto.AddResultRequest;
import moodi.backend.result.dto.ResultResponse;
import moodi.backend.result.dto.UpdateResultRequest;
import moodi.backend.result.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형태로 반환
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/results")
    public ResponseEntity<Result> addResult(@RequestBody AddResultRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // JWT에 저장된 username
        Result savedResult = resultService.save(request, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedResult);
    }

    @GetMapping("/results")
    public ResponseEntity<List<ResultResponse>> findAllResults() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // JWT에 저장된 username
        List<ResultResponse> results = resultService.findAll()
                .stream()
                .map(ResultResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(results);
    }

    @PutMapping("/results/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable Long id, @RequestBody UpdateResultRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Result updatedResult = resultService.update(id, request, username);
        return ResponseEntity.ok(updatedResult);
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<String> deleteResult(@PathVariable Long id) {
        try {
            resultService.deleteResult(id);
            return ResponseEntity.ok("Result deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found.");
        }
    }
}
