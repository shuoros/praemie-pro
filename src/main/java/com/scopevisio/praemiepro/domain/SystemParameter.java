package com.scopevisio.praemiepro.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "system_parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemParameter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "base_insurance_price", precision = 21, scale = 2)
    private BigDecimal baseInsurancePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBaseInsurancePrice() {
        return baseInsurancePrice;
    }

    public void setBaseInsurancePrice(BigDecimal baseInsurancePrice) {
        this.baseInsurancePrice = baseInsurancePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemParameter)) {
            return false;
        }
        return getId() != null && getId().equals(((SystemParameter) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SystemParameter{" +
                "id=" + id +
                ", baseInsurancePrice=" + baseInsurancePrice +
                '}';
    }
}
