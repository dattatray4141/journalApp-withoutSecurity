package com.journal.service;

import com.journal.external_api_response.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class WeatherService {

    //    @Value("${weather.api.key}")
    private static final String apiKey = "df03baabc973bf01789b64b8366ef13f";

    private static final String API = "https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=API_KEY&units=metric";


    RestTemplate restTemplate;
    RedisService redisService;

    //Instead of we can also create bean in main class.
    public WeatherService(RestTemplateBuilder builder, RedisService redisService) {
        restTemplate = builder.build();
        this.redisService = redisService;
    }

    //It uses finalApi, then It sends an HTTP GET request to the weather API and converts the JSON response into a WeatherResponse Java object.
    //Deserialization (JSON → Java object
    //Get Weather details using redis cache technique
    //Suppose you hit same city multiple times you do not need to connect t o db every time , we can get by redis cache
    public WeatherResponse getWeather(String city) {
        //This is cache response
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        } else {
            String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
            log.info("API Key:" + finalAPI);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();//value is getting from db and then set in cache to next line
            if (body != null) {
                redisService.set("weather_of_" + city, body, 300l);
                log.info("Weather set successfully");
            }
            return body;
        }
    }


    //Without redis method -- > if you want call this call from weatherController class
        public WeatherResponse getWeatherWithoutRedis(String city) {
            //This is cache response
               redisService.get("weather_of_" + city, WeatherResponse.class);
                String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
                log.info("API Key:" + finalAPI);
                ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
                WeatherResponse body = response.getBody();//value is getting from db and then set in cache to next line
                return body;
            }
}
