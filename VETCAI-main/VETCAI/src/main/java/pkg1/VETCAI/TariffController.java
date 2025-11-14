package pkg1.VETCAI;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tariffs")
@CrossOrigin(origins = "*")
public class TariffController {
    private final TariffSettingRepository tariffRepo;
    private final DistrictRepository districtRepo;

    public TariffController(TariffSettingRepository tariffRepo, DistrictRepository districtRepo) {
        this.tariffRepo = tariffRepo;
        this.districtRepo = districtRepo;
    }

    @GetMapping
    public List<TariffSetting> getAll() {
        return tariffRepo.findAll();
    }

    @GetMapping("/{districtId}")
    public ResponseEntity<?> getByDistrict(@PathVariable Integer districtId) {
        Optional<TariffSetting> ts = tariffRepo.findByDistrict_DistrictId(districtId);
        return ts.<ResponseEntity<?>>map(ResponseEntity::ok)
                 .orElseGet(() -> ResponseEntity.status(404).body(Map.of("message", "Not found")));
    }

    public static class TariffUpdate {
        public Double lowLimit;
        public Double mediumLimit;
        public Double highLimit;
    }

    @PutMapping("/{districtId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Integer districtId, @RequestBody TariffUpdate body) {
        Optional<District> d = districtRepo.findById(districtId);
        if (d.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "District not found"));
        }
        TariffSetting ts = tariffRepo.findByDistrict_DistrictId(districtId).orElseGet(() -> {
            TariffSetting n = new TariffSetting();
            n.setDistrict(d.get());
            n.setDistrictId(districtId);
            return n;
        });
        if (body.lowLimit != null) ts.setLowLimit(body.lowLimit);
        if (body.mediumLimit != null) ts.setMediumLimit(body.mediumLimit);
        if (body.highLimit != null) ts.setHighLimit(body.highLimit);
        tariffRepo.save(ts);
        return ResponseEntity.ok(Map.of("success", true));
    }
}