package pkg1.VETCAI;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "tariff_settings")
public class TariffSetting {
    @Id
    private Integer districtId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @Column(name = "low_limit")
    private Double lowLimit;
    @Column(name = "medium_limit")
    private Double mediumLimit;
    @Column(name = "high_limit")
    private Double highLimit;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public TariffSetting() {
    }

    public TariffSetting(District district, Double lowLimit, Double mediumLimit, Double highLimit, LocalDateTime lastUpdated) {
        this.district = district;
        this.lowLimit = lowLimit;
        this.mediumLimit = mediumLimit;
        this.highLimit = highLimit;
        this.lastUpdated = lastUpdated;
    }

    public Integer getDistrictId() { return districtId; }
    public void setDistrictId(Integer districtId) { this.districtId = districtId; }

    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }

    public Double getLowLimit() { return lowLimit; }
    public void setLowLimit(Double lowLimit) { this.lowLimit = lowLimit; }

    public Double getMediumLimit() { return mediumLimit; }
    public void setMediumLimit(Double mediumLimit) { this.mediumLimit = mediumLimit; }

    public Double getHighLimit() { return highLimit; }
    public void setHighLimit(Double highLimit) { this.highLimit = highLimit; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}

