package pkg1.VETCAI;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usage")
@CrossOrigin(origins = "*")
public class UsageController {
    private final UsageDataRepository usageRepo;

    public UsageController(UsageDataRepository usageRepo) {
        this.usageRepo = usageRepo;
    }

    @GetMapping("/{districtId}")
    public List<UsageData> byDistrict(@PathVariable Integer districtId) {
        return usageRepo.findByDistrict_DistrictId(districtId);
    }
}