package com.monich.cost_price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class CostPriceApiServer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CostPriceApiServer.class);
        springApplication.addListeners(
                new ApplicationPidFileWriter("cost_price_api.pid"));
        springApplication.run(args);
    }

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
