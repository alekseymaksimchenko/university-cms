package com.foxminded.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UniversityApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniversityApp.class);

    public static void main(String[] args) {
        LOGGER.info("STARTING THE APPLICATION");
        SpringApplication application = new SpringApplicationBuilder(UniversityApp.class)
                .build();
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        LOGGER.info("APPLICATION IS RUNING......");
    }

}
