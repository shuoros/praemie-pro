package com.scopevisio.praemiepro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scopevisio.praemiepro.domain.enumeration.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "p_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order extends AbstractAuditEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "yearly_drive")
    private Integer yearlyDrive;

    @Column(name = "yearly_price", precision = 21, scale = 2)
    private BigDecimal yearlyPrice;

    @Size(max = 5)
    @Column(name = "zipcode", length = 5)
    private String zipcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"orders"}, allowSetters = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getYearlyDrive() {
        return yearlyDrive;
    }

    public void setYearlyDrive(Integer yearlyDrive) {
        this.yearlyDrive = yearlyDrive;
    }

    public BigDecimal getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(BigDecimal yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", vehicleType=" + vehicleType +
                ", yearlyDrive=" + yearlyDrive +
                ", yearlyPrice=" + yearlyPrice +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
