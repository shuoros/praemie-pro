package com.scopevisio.praemiepro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Region implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "region_1")
    private String region1;

    @Column(name = "region_2")
    private String region2;

    @Column(name = "region_3")
    private String region3;

    @Size(max = 5)
    @Column(name = "zipcode", length = 5)
    private String zipcode;

    @Column(name = "location")
    private String location;

    @Column(name = "area_1")
    private String area1;

    @Column(name = "area_2")
    private String area2;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "utc")
    private String utc;

    @Column(name = "is_summer_time")
    private Boolean isSummerTime;

    @Column(name = "active")
    private String active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "regions" }, allowSetters = true)
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion1() {
        return region1;
    }

    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    public String getRegion2() {
        return region2;
    }

    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    public String getRegion3() {
        return region3;
    }

    public void setRegion3(String region3) {
        this.region3 = region3;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea1() {
        return area1;
    }

    public void setArea1(String area1) {
        this.area1 = area1;
    }

    public String getArea2() {
        return area2;
    }

    public void setArea2(String area2) {
        this.area2 = area2;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public Boolean IsSummerTime() {
        return isSummerTime;
    }

    public void setSummerTime(Boolean isSummerTime) {
        this.isSummerTime = isSummerTime;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return getId() != null && getId().equals(((Region) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", region1='" + region1 + '\'' +
                ", region2='" + region2 + '\'' +
                ", region3='" + region3 + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", location='" + location + '\'' +
                ", area1='" + area1 + '\'' +
                ", area2='" + area2 + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", timezone='" + timezone + '\'' +
                ", utc='" + utc + '\'' +
                ", isSummerTime=" + isSummerTime +
                ", active='" + active + '\'' +
                '}';
    }
}
