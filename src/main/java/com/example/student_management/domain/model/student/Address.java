package com.example.student_management.domain.model.student;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object representing Address
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    public Address(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (street != null) sb.append(street).append(", ");
        if (city != null) sb.append(city).append(", ");
        if (state != null) sb.append(state).append(" ");
        if (postalCode != null) sb.append(postalCode).append(", ");
        if (country != null) sb.append(country);
        return sb.toString().replaceAll(", $", "");
    }

    @Override
    public String toString() {
        return getFullAddress();
    }
}

