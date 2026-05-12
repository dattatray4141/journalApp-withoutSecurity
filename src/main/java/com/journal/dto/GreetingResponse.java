package com.journal.dto;

import lombok.*;


//Made for external api purpose -- Created for Weather
//used to return only needed data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GreetingResponse {
    private String city;
    private double temperature;
    private double feelsLike;
    private String description;

}
