package com.scopevisio.praemiepro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class State implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(max = 5)
    @Column(name = "code", length = 5)
    private String code;

    @NotNull
    @Column(name = "insurance_factor")
    private Float insuranceFactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "states" }, allowSetters = true)
    private Country country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "state" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull @Size(max = 5) String getCode() {
        return code;
    }

    public void setCode(@NotNull @Size(max = 5) String code) {
        this.code = code;
    }

    public Float getInsuranceFactor() {
        return insuranceFactor;
    }

    public void setInsuranceFactor(Float insuranceFactor) {
        this.insuranceFactor = insuranceFactor;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        return getId() != null && getId().equals(((State) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", insuranceFactor=" + insuranceFactor +
                '}';
    }
}
