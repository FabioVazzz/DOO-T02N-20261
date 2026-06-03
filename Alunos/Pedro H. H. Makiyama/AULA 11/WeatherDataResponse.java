package com.pedro;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDataResponse {

    private List<WeatherDataDay> days;

    public List<WeatherDataDay> getDays() {
        return days;
    }

    public void setDays(List<WeatherDataDay> days) {
        this.days = days;
    }
}
