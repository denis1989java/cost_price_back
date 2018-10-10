package com.monich.cost_price.api.config;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        List<ResponseMessage> responses = ImmutableList.of(
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Request validation error")
                        .responseModel(new ModelRef("ValidationErrorDto"))
                        .build(),
                new ResponseMessageBuilder()
                        .code(403)
                        .message("Forbidden!")
                        .build(),

                new ResponseMessageBuilder()
                        .code(500)
                        .message("Server error")
                        .responseModel(new ModelRef("ErrorDto"))
                        .build());


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monich.cost_price.api.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responses)
                .globalResponseMessage(RequestMethod.GET, responses)
                .globalResponseMessage(RequestMethod.PUT, responses)
                .globalResponseMessage(RequestMethod.DELETE, responses);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "ACBM REST API",
                "Public API for ACBM operations.",
                "1.0",
                "Terms of service",
                new Contact("John Doe", "www.example.com", "myeaddress@company.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

}
