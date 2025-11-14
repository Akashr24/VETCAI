package pkg1.VETCAI;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsageDataRepository extends JpaRepository<UsageData, Integer> {
    List<UsageData> findByDistrict_DistrictId(Integer districtId);
}
