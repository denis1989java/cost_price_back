package com.monich.cost_price.service;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class MaxmindService {

    private final DatabaseReader cityByIp;
    private final DatabaseReader countryByIp;

    @AllArgsConstructor
    class GeoInfo {
        final String country;
        final String city;
    }

    public MaxmindService(
            @Value("${maxmind.database.country.path}") String databaseCountryPath,
            @Value("${maxmind.database.city.path}") String databaseCityPath) throws IOException {
        countryByIp = new DatabaseReader.Builder(new File(databaseCountryPath))
                .withCache(new CHMCache()).build();
        cityByIp = new DatabaseReader.Builder(new File(databaseCityPath))
                .withCache(new CHMCache())
                .build();
    }

    GeoInfo getGeoInfoByIpIp(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            try {
                CityResponse response = cityByIp.city(ipAddress);
                return new GeoInfo(response.getCountry().getIsoCode(),
                        response.getCity().getName());
            } catch (IOException e) {
                log.error("GEO information search error", e);
                return new GeoInfo(null, null);
            } catch (GeoIp2Exception e) {
                log.warn("City information is not available for ip {}", ip);
                return new GeoInfo(getCountryNameByIp(ipAddress), null);
            }
        } catch (UnknownHostException e) {
            log.warn("Wrong IP: {}", ip);
            return new GeoInfo(null, null);
        }
    }

    private String getCountryNameByIp(InetAddress ip) {
        try {
            CountryResponse response = countryByIp.country(ip);
            if (response != null) {
                return response.getCountry().getIsoCode();
            } else {
                return null;
            }
        } catch (GeoIp2Exception e) {
            log.warn("Country information is not available for ip {}", ip.toString());
            return null;
        } catch (IOException e) {
            log.error("GEO information search error", e);
            return null;
        }
    }
}
