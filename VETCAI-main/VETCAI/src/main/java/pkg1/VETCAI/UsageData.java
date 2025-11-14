package pkg1.VETCAI;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "usage_data")
public class UsageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usageId;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    private LocalDateTime timestamp = LocalDateTime.now();
    @Column(name = "power_usage")
    private Double powerUsage;
    private Double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff_level")
    private TariffLevel tariffLevel;

    public UsageData() {
    }

    public UsageData(Integer usageId, District district, LocalDateTime timestamp, Double powerUsage, Double cost, TariffLevel tariffLevel) {
        this.usageId = usageId;
        this.district = district;
        this.timestamp = timestamp;
        this.powerUsage = powerUsage;
        this.cost = cost;
        this.tariffLevel = tariffLevel;
    }

    public Integer getUsageId() { return usageId; }
    public void setUsageId(Integer usageId) { this.usageId = usageId; }

    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Double getPowerUsage() { return powerUsage; }
    public void setPowerUsage(Double powerUsage) { this.powerUsage = powerUsage; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public TariffLevel getTariffLevel() { return tariffLevel; }
    public void setTariffLevel(TariffLevel tariffLevel) { this.tariffLevel = tariffLevel; }

    public enum TariffLevel {
        Low, Medium, High
    }
}

