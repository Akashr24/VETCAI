package pkg1.VETCAI;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TariffSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tariffId;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    private Double lowLimit;
    private Double mediumLimit;
    private Double highLimit;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    public TariffSetting() {
    }

    public TariffSetting(Integer tariffId, District district, Double lowLimit, Double mediumLimit, Double highLimit, LocalDateTime lastUpdated) {
        this.tariffId = tariffId;
        this.district = district;
        this.lowLimit = lowLimit;
        this.mediumLimit = mediumLimit;
        this.highLimit = highLimit;
        this.lastUpdated = lastUpdated;
    }

    public Integer getTariffId() { return tariffId; }
    public void setTariffId(Integer tariffId) { this.tariffId = tariffId; }

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

