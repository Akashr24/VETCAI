package pkg1.VETCAI;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final AdminUserRepository adminRepo;

    public AdminController(AdminUserRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest req) {
        if (req == null || req.username == null || req.password == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing credentials"));
        }
        Optional<AdminUser> user = adminRepo.findByUsernameAndPassword(req.username, req.password);
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid username or password"));
        }
        String token = UUID.randomUUID().toString();
        return ResponseEntity.ok(Map.of("success", true, "token", token));
    }
}