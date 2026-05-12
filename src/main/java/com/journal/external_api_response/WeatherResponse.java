package com.journal.external_api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//Made for only Weather practice
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("main")
    private Main main;

    @JsonProperty("weather")
    private List<Weather> weather;

    // 🔥 helper methods (what YOU actually use)
    public int getTemperature() {
        return (int) main.getTemp();
    }

    public int getFeelsLike() {
        return (int) main.getFeelsLike();
    }

    public String getDescription() {
        return weather != null && !weather.isEmpty()
                ? weather.get(0).getDescription()
                : null;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        @JsonProperty("temp")
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        @JsonProperty("description")
        private String description;
    }
}