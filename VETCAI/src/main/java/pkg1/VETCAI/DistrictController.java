package pkg1.VETCAI;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {

    private final DistrictRepository districtRepo;

    public DistrictController(DistrictRepository districtRepo) {
        this.districtRepo = districtRepo;
    }

    @GetMapping
    public List<District> getAll() {
        return districtRepo.findAll();
    }

    @PostMapping
    public District create(@RequestBody District district) {
        return districtRepo.save(district);
    }
}

