package com.monich.cost_price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class CostPriceOauthServer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CostPriceOauthServer.class);
        springApplication.addListeners(
                new ApplicationPidFileWriter("cost_price_oauth.pid"));
        springApplication.run(args);
    }

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
