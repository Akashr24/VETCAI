package pkg1.VETCAI;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/led")
@CrossOrigin(origins = "*")
public class LedController {
    private final LedService ledService;

    public LedController(LedService ledService) {
        this.ledService = ledService;
    }

    @PostMapping("/{color}/on")
    public ResponseEntity<Map<String, Object>> turnOn(@PathVariable String color) {
        if (!ledService.isConfigured()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ESP32 URL not configured"));
        }
        boolean ok = ledService.sendCommand(color, true);
        return ok ? ResponseEntity.ok(Map.of("success", true)) : ResponseEntity.status(502).body(Map.of("success", false, "message", "Failed to send command"));
    }

    @PostMapping("/{color}/off")
    public ResponseEntity<Map<String, Object>> turnOff(@PathVariable String color) {
        if (!ledService.isConfigured()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ESP32 URL not configured"));
        }
        boolean ok = ledService.sendCommand(color, false);
        return ok ? ResponseEntity.ok(Map.of("success", true)) : ResponseEntity.status(502).body(Map.of("success", false, "message", "Failed to send command"));
    }
}
