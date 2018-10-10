package com.monich.cost_price.core.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "country", length = 2)
    private String country;
    @Column(name = "city", length = 150)
    private String city;
    @Column(name = "street", length = 150)
    private String street;
    @Column(name = "zip", length = 10)
    private String zip;

    @Override
    public String toString() {
        String countryName = new Locale("en", country).getDisplayCountry().toUpperCase();
        return Stream.of(street, city, zip, countryName).filter(s -> !StringUtils.isEmpty(s))
                .collect(Collectors.joining("\n"));
    }
}
