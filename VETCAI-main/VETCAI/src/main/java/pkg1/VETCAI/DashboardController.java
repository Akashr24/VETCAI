package pkg1.VETCAI;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DistrictRepository districtRepo;
    private final UsageDataRepository usageRepo;

    public DashboardController(DistrictRepository districtRepo, UsageDataRepository usageRepo) {
        this.districtRepo = districtRepo;
        this.usageRepo = usageRepo;
    }

    @GetMapping("/district-summary")
    public List<Map<String, Object>> getSummary() {
        // Load all usage data once and group by district id to avoid repeated DB calls
        List<UsageData> allUsage = usageRepo.findAll();
        Map<Integer, List<UsageData>> usageByDistrict = allUsage.stream()
                .filter(u -> u.getDistrict() != null && u.getDistrict().getDistrictId() != null)
                .collect(Collectors.groupingBy(u -> u.getDistrict().getDistrictId()));

        return districtRepo.findAll().stream().map(d -> {
            List<UsageData> data = usageByDistrict.getOrDefault(d.getDistrictId(), Collections.emptyList());

        double avgUsage = data.stream()
            .map(UsageData::getPowerUsage)
            .filter(java.util.Objects::nonNull)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        double avgCost = data.stream()
            .map(UsageData::getCost)
            .filter(java.util.Objects::nonNull)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

            // Determine latest tariff level by timestamp if available
            String currentLevel = "N/A";
            Optional<UsageData> latest = data.stream().filter(u -> u.getTimestamp() != null).max(Comparator.comparing(UsageData::getTimestamp));
            if (latest.isPresent() && latest.get().getTariffLevel() != null) {
                currentLevel = latest.get().getTariffLevel().name();
            } else if (!data.isEmpty() && data.get(data.size() - 1).getTariffLevel() != null) {
                // fallback to last element's level if timestamps are missing
                currentLevel = data.get(data.size() - 1).getTariffLevel().name();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("districtName", d.getDistrictName());
            map.put("providerName", d.getProviderName());
            map.put("avgUsage", avgUsage);
            map.put("avgCost", avgCost);
            map.put("currentLevel", currentLevel);
            return map;
        }).collect(Collectors.toList());
    }
}

