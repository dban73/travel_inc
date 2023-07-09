package com.benitez.best_travel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class BestTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestTravelApplication.class, args);
    }

}
