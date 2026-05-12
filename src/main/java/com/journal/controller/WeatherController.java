package com.journal.controller;

import com.journal.dto.GreetingResponse;
import com.journal.external_api_response.WeatherResponse;
import com.journal.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/getweather/city")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    //Get weather by city API
    //We use GreetingResponse to send only the needed data in a clean format, instead of giving the full raw weather API response.
    @GetMapping("/{city}")
    public ResponseEntity<?> greetings(@PathVariable String city) {

        try {
            //Change the method name to get data with redis or without redis
            WeatherResponse weatherResponse = weatherService.getWeather(city);

            GreetingResponse response = new GreetingResponse(
                    city,
                    weatherResponse.getTemperature(),
                    weatherResponse.getFeelsLike(),
                    weatherResponse.getDescription()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("City name provided by user:" + city, e.getMessage());
            return new ResponseEntity<>(city + " city not found... ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
